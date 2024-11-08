package org.wtm.web.admin.service;

import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.admin.dto.dashboard.DashboardDto;
import org.wtm.web.admin.dto.info.StoreInfoDto;
import org.wtm.web.admin.dto.info.StoreInfoUpdateDto;

public interface AdminStoreService {
    /**
     * 대시보드 조회
     */
    public DashboardDto getDashboardByStoreId(Long storeId);

    StoreInfoDto getStoreInfoByStoreId(Long storeId);

    void updateStoreInfoByStoreId(Long storeId, StoreInfoUpdateDto updateDto, MultipartFile img);
}
