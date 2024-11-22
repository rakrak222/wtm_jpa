package org.wtm.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.admin.dto.dashboard.DashboardDto;
import org.wtm.web.admin.dto.info.StoreInfoDto;
import org.wtm.web.admin.dto.info.StoreInfoUpdateDto;
import org.wtm.web.admin.mapper.AdminStoreMapper;
import org.wtm.web.admin.repository.*;
import org.wtm.web.auth.dto.Address;
import org.wtm.web.common.service.FileUploadService;
import org.wtm.web.store.model.Store;
import org.wtm.web.store.model.StoreSns;
import org.wtm.web.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultAdminStoreService implements AdminStoreService {

    private final AdminStoreRepository storeRepository;
    private final AdminStoreMapper adminStoreMapper;
    private final AdminStoreSnsRepository storeSnsRepository;
    private final AdminUserRepository adminUserRepository;
    private final FileUploadService uploadService;

    @Value("${image.upload-profile-dir}")
    String uploadDir;

    @Override
    @Transactional(readOnly = true)
    public DashboardDto getDashboardByStoreId(Long storeId) {
        Store store =
                storeRepository.findById(storeId)
                        .orElseThrow(() -> new IllegalArgumentException("스토어를 찾을 수 없습니다."));

        return adminStoreMapper.toDashboardDto(store);
    }

    @Override
    @Transactional
    public StoreInfoDto getStoreInfoByStoreId(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("스토어를 찾을 수 없습니다."));
        User user = store.getUser();
        if (user == null) {
            throw new IllegalArgumentException("해당 가게의 유저 정보를 불러올 수 없습니다.");
        }
        List<StoreSns> storeSnsList = storeSnsRepository.findByStoreId(storeId)
                .orElseThrow(()-> new IllegalArgumentException("해당 가게의 sns 정보를 찾을 수 없습니다."));
        List<String> snsAddressList = storeSnsList.stream().map(StoreSns::getUrl).toList();

        StoreInfoDto storeInfoDto = StoreInfoDto.builder()
                .storeId(store.getId())
                .profilePicture(user.getProfilePicture())
                .storeName(store.getName())
                .storeAddress(store.getAddress())
                .snsAddress(snsAddressList)
                .phone(store.getContact())
                .openTime(store.getOpenTime())
                .closeTime(store.getCloseTime())
                .build();

        return storeInfoDto;
    }

    @Override
    @Transactional
    public void updateStoreInfoByStoreId(Long storeId, StoreInfoUpdateDto updateDto, MultipartFile img) {
        // 1. Store 정보 가져오기
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("스토어를 찾을 수 없습니다."));

        // 2. User 정보 가져오기
        User user = store.getUser();
        if (user == null) {
            throw new IllegalArgumentException("해당 스토어의 관리자를 찾을 수 없습니다.");
        }

        // 2-1. User 프로필 사진 업데이트
        if (img != null) {
            String savedImgUrl = uploadService.uploadFile(img, uploadDir);
            user.updateProfilePicture(savedImgUrl);
        }

        // 3. StoreSns 정보 가져오기
        List<StoreSns> existingSnsList = storeSnsRepository.findByStoreId(storeId).orElse(List.of());

        // 4. 기존 Store 엔티티의 필드 업데이트
        if (updateDto.getStoreName() != null) {
            store.updateName(updateDto.getStoreName());
        }
        if (updateDto.getAddress() != null) {
            store.updateAddress(updateDto.toAddress());
        }
        System.out.println(updateDto.getPostcode());
        System.out.println(updateDto.getAddress());
        System.out.println(updateDto.getDetailAddress());
        if (updateDto.getPhone() != null) {
            store.updateContact(updateDto.getPhone());
        }
        if (updateDto.getOpenTime() != null) {
            store.updateOpenTime(updateDto.getOpenTime());
        }
        if (updateDto.getCloseTime() != null) {
            store.updateCloseTime(updateDto.getCloseTime());
        }

        // SNS 주소 업데이트 로직
        if (updateDto.getSnsAddress() != null) {
            List<String> updatedSnsAddresses = new ArrayList<>(updateDto.getSnsAddress());

            // 기존 SNS 업데이트 또는 삭제
            for (StoreSns existingSns : existingSnsList) {
                if (updatedSnsAddresses.contains(existingSns.getUrl())) {
                    // 기존 주소가 업데이트 목록에 있으면 그대로 유지
                    updatedSnsAddresses.remove(existingSns.getUrl()); // 남은 것은 새로운 주소
                } else {
                    // 업데이트 목록에 없는 기존 주소는 삭제
                    storeSnsRepository.delete(existingSns);
                }
            }

            // 새롭게 추가된 SNS 주소 저장
            for (String newSnsUrl : updatedSnsAddresses) {
                StoreSns newSns = StoreSns.builder()
                        .type("SNS") // 기본 타입 설정 또는 적절한 타입 값 전달
                        .url(newSnsUrl)
                        .store(store)
                        .build();
                storeSnsRepository.save(newSns);
            }
        }

        // 5. 저장
        // 기존 엔티티를 수정했으므로, 별도의 save 메서드를 호출하지 않아도 @Transactional에 의해 자동 저장됩니다.
        // 하지만 명시적으로 호출하고 싶다면 아래와 같이 호출할 수 있습니다.
        storeRepository.save(store);
        adminUserRepository.save(user);
    }


}
