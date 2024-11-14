package org.wtm.web.store.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StoreAddressResponseDto {
    private String storeName;
    private String address;
}
