package org.wtm.web.store.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtm.web.common.repository.StoreRepository;
import org.wtm.web.store.dto.StoreDetailResponseDto;
import org.wtm.web.store.dto.StoreResponseDto;
import org.wtm.web.store.dto.StoreReviewStatsDto;
import org.wtm.web.store.mapper.StoreDetailMapper;
import org.wtm.web.store.mapper.StoreMapper;
import org.wtm.web.store.mapper.StoreReviewStatsMapper;
import org.wtm.web.store.model.Store;
import org.wtm.web.store.model.StoreSns;
import org.wtm.web.store.service.StoreService;
import org.wtm.web.ticket.model.Ticket;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultStoreService implements StoreService {

    private final StoreRepository storeRepository;
    private final StoreDetailMapper storeDetailMapper;


    @Override
    @Transactional
    public List<StoreResponseDto> getAllStores() {        // 1. StoreRepository에서 fetch join을 통해 데이터를 가져옴
        List<Store> stores = storeRepository.findAllWithDetails();

        // 2. 가져온 데이터를 DTO로 변환
        List<StoreResponseDto> result = stores.stream()
                .map(StoreMapper::toDto)
                .collect(Collectors.toList());

        // 3. 결과 반환
        return result;
    }

    @Override
    @Transactional
    public StoreDetailResponseDto getStoreDetailsById(Long storeId) {
        // Store, StoreSns, Ticket 데이터를 한 번에 조회
        Optional<Store> storeOptional = storeRepository.findStoreDetailsById(storeId);
        if (storeOptional.isEmpty()) {
            return null;
        }

        Store store = storeOptional.get();
        List<StoreSns> storeSnsList = storeRepository.findStoreSnsById(storeId);
        List<Ticket> ticketList = storeRepository.findTicketsByStoreId(storeId);

        // StoreDetailMapper를 사용하여 필요한 필드만 가진 DTO로 변환
        return storeDetailMapper.toDto(store, storeSnsList, ticketList);
    }

    public StoreReviewStatsDto getStoreReviewStats(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("가게를 찾을수 없습니다."));
        Object[] stats = storeRepository.findReviewStateByStoreId(storeId);
        Long reviewCount = (Long) stats[0];
        Double averageReviewScore = (Double) stats[1];

        return StoreReviewStatsMapper.toDto(store, reviewCount, averageReviewScore);
    }
}
