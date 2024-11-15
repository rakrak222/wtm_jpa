package org.wtm.web.admin.dto.info;

import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wtm.web.auth.dto.Address;
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
    @Embedded
    private Address storeAddress;
    private List<String> snsAddress;
    private String phone;
    private LocalTime openTime;
    private LocalTime closeTime;
}
