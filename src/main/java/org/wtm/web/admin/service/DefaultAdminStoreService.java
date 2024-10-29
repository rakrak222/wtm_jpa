package org.wtm.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtm.web.admin.dto.dashboard.DashboardDto;
import org.wtm.web.admin.mapper.AdminStoreMapper;
import org.wtm.web.admin.repository.*;
import org.wtm.web.store.model.Store;

@Service
@RequiredArgsConstructor
public class DefaultAdminStoreService implements AdminStoreService {

    private final AdminStoreRepository storeRepository;
    private final AdminStoreMapper storeMapper;

    /**
     * 대시보드 조회
     * 수정해야함.
     */
    public DashboardDto getDashboardByStoreId(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("스토어를 찾을 수 없습니다."));

        return storeMapper.toDashboardDto(store);
    }



}
