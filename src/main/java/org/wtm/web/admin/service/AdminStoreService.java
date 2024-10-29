package org.wtm.web.admin.service;

import org.wtm.web.admin.dto.dashboard.DashboardDto;

public interface AdminStoreService {
    /**
     * 대시보드 조회
     */
    public DashboardDto getDashboardByStoreId(Long storeId);
}
