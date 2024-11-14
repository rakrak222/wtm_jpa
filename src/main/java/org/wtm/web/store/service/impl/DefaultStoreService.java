package org.wtm.web.store.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtm.web.common.repository.StoreRepository;
import org.wtm.web.store.dto.StoreAddressResponseDto;
import org.wtm.web.store.dto.StoreDetailResponseDto;
import org.wtm.web.store.dto.StoreResponseDto;
import org.wtm.web.store.dto.StoreReviewStatsDto;
import org.wtm.web.store.mapper.StoreDetailMapper;
import org.wtm.web.store.mapper.StoreMapper;
import org.wtm.web.store.mapper.StoreReviewStatsMapper;
import org.wtm.web.store.model.Store;
import org.wtm.web.store.model.StoreSns;
import org.wtm.web.store.service.StoreService;
import org.wtm.web.ticket.model.Ticket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultStoreService implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreDetailMapper storeDetailMapper;



    // 모든 가게 조회(검색기능 없는 버전)
    @Override
    @Transactional
    public List<StoreResponseDto> getAllStores() {        // 1. StoreRepository에서 fetch join을 통해 데이터를 가져옴
        List<Store> stores = storeRepository.findAllWithDetails();

        // 2. 가져온 데이터를 DTO로 변환
        List<StoreResponseDto> result = stores.stream()
                .map(StoreMapper::toDto)
                .collect(Collectors.toList());

        // 3. 결과 반환
        return result;
    }


    // 가게 조회(검색 기능 포함 - 이름 및 오늘의 메뉴)
    @Override
    @Transactional
    public List<StoreResponseDto> getStores(String query){

        List<Store> stores;

        if(query != null && !query.isEmpty()) {
            stores = storeRepository.searchStoresByNameOrTodayMenu(query);
        } else {
            stores = storeRepository.findAllWithDetails();
        }

        List<StoreResponseDto> result = stores.stream()
                .map(StoreMapper::toDto)
                .collect(Collectors.toList());

        return result;
    }
    @Override
    @Transactional
    public List<StoreAddressResponseDto> getStoresAddress() {


        List<Store> stores = storeRepository.findAllWithDetails();

        // Entity를 DTO로 변환
        return stores.stream()
                .map(store -> StoreAddressResponseDto.builder()
                        .storeName(store.getName())
                        .address(store.getAddress())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public StoreDetailResponseDto getStoreDetailsById(Long storeId) {
        // Store, StoreSns, Ticket 데이터를 한 번에 조회
        Optional<Store> storeOptional = storeRepository.findStoreDetailsById(storeId);
        if (storeOptional.isEmpty()) {
            return null;
        }

        Store store = storeOptional.get();
        List<StoreSns> storeSnsList = storeRepository.findStoreSnsById(storeId);
        List<Ticket> ticketList = storeRepository.findTicketsByStoreId(storeId);

        // StoreDetailMapper를 사용하여 필요한 필드만 가진 DTO로 변환
        return storeDetailMapper.toDto(store, storeSnsList, ticketList);
    }



    @Override
    @Transactional
    public StoreReviewStatsDto getStoreReviewStats(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

        // 쿼리 결과를 List<Object[]>로 받기
        List<Object[]> statsList = storeRepository.findReviewStateByStoreId(storeId);

        // statsList가 비어있는지 확인하고 첫 번째 항목 가져오기
        Object[] stats = (statsList != null && !statsList.isEmpty()) ? statsList.get(0) : new Object[]{0, 0.0};

        // 디버깅을 위한 출력
        System.out.println("stats = " + Arrays.toString(stats));

        // 배열의 길이를 확인하여 값이 없을 때 기본값 설정
        Long reviewCount = (stats.length > 0 && stats[0] instanceof Number) ? ((Number) stats[0]).longValue() : 0L;
        Double averageReviewScore = (stats.length > 1 && stats[1] instanceof Number) ? ((Number) stats[1]).doubleValue() : 0.0;

        return StoreReviewStatsMapper.toDto(store, reviewCount, averageReviewScore);
    }
}
