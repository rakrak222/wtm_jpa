package org.wtm.web.admin.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardDto {
    private Long storeId;
    private String storeName;
    private String userProfilePicture;
}
