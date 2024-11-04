package org.wtm.web.store.mapper;

import org.wtm.web.store.dto.StoreReviewStatsDto;
import org.wtm.web.store.model.Store;

public class StoreReviewStatsMapper {

    public static StoreReviewStatsDto toDto(Store store, Long reviewCount, Double averageReviewScore){
        return StoreReviewStatsDto.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .reviewCount(reviewCount)
                .averageReviewScore(averageReviewScore != null ? averageReviewScore : 0.0)
                .build();
    }
}
