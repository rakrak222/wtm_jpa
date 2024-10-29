package org.wtm.web.store.service;



import org.wtm.web.store.dto.StoreDetailResponseDto;
import org.wtm.web.store.dto.StoreResponseDto;

import java.util.List;


public interface StoreService {

    List<StoreResponseDto> getAllStores();

    StoreDetailResponseDto getStoreDetailsById(Long storeId);
}
