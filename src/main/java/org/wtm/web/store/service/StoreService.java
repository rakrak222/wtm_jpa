package org.wtm.web.store.service;



import org.wtm.web.store.dto.StoreDetailResponseDto;
import org.wtm.web.store.dto.StoreResponseDto;
import org.wtm.web.store.dto.StoreReviewStatsDto;

import java.util.List;


public interface StoreService {

    List<StoreResponseDto> getAllStores();

    List<StoreResponseDto> getStores(String query);

    StoreReviewStatsDto getStoreReviewStats(Long storeId);

    StoreDetailResponseDto getStoreDetailsById(Long storeId);
}
