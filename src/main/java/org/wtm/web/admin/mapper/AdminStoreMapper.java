package org.wtm.web.admin.mapper;

import org.springframework.stereotype.Component;
import org.wtm.web.admin.dto.dashboard.DashboardDto;
import org.wtm.web.store.model.Store;

@Component
public class AdminStoreMapper {

    public DashboardDto toDashboardDto(Store store) {
        return new DashboardDto(
                store.getId(),
                store.getName(),
                store.getUser().getProfilePicture()
        );
    }
}
