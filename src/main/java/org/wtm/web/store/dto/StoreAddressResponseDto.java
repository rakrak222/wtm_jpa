package org.wtm.web.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StoreAddressResponseDto {
    private Long storeId;
    private String storeName;
    private String address;
    private Double latitude;    // 추가된 필드
    private Double longitude;   // 추가된 필드
}
