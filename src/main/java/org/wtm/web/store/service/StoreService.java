package org.wtm.web.store.service;



import java.util.Optional;
import org.wtm.web.store.dto.StoreAddressResponseDto;
import org.wtm.web.store.dto.StoreDetailResponseDto;
import org.wtm.web.store.dto.StoreResponseDto;
import org.wtm.web.store.dto.StoreReviewStatsDto;

import java.util.List;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;


public interface StoreService {

    List<StoreResponseDto> getAllStores();

    List<StoreResponseDto> getStores(String query);

    StoreReviewStatsDto getStoreReviewStats(Long storeId);

    StoreDetailResponseDto getStoreDetailsById(Long storeId);

    List<StoreAddressResponseDto> getStoresAddress();

    String getDirections(Long storeId, double userLatitude, double userLongitude);

    Optional<Store> getStoreByUser(User user);
}
