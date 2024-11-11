package org.wtm.web.admin.dto.info;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wtm.web.store.model.StoreSns;

import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreInfoDto {

    private Long storeId;
    private String profilePicture;
    private String storeName;
    private String storeAddress;
    private List<String> snsAddress;
    private String phone;
    private LocalTime openTime;
    private LocalTime closeTime;
}
