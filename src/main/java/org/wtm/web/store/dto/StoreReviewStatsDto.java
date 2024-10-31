package org.wtm.web.store.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreReviewStatsDto {
    private Long storeId;
    private String storeName;
    private Long reviewCount;
    private Double averageReviewScore;
}
