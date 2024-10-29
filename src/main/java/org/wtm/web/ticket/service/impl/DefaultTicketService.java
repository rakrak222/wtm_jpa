package org.wtm.web.ticket.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtm.web.common.repository.StoreRepository;
import org.wtm.web.common.repository.TicketHistoryPurchaseRepository;
import org.wtm.web.common.repository.TicketHistoryUsageRepository;
import org.wtm.web.store.model.Store;
import org.wtm.web.ticket.dto.TicketResponseDto;
import org.wtm.web.ticket.service.TicketService;

@Service
@RequiredArgsConstructor
public class DefaultTicketService implements TicketService {
    private final StoreRepository storeRepository;
    private final TicketHistoryPurchaseRepository ticketHistoryPurchaseRepository;
    private final TicketHistoryUsageRepository ticketHistoryUsageRepository;

    public TicketResponseDto getTicketsByStoreId(Long storeId, Long userId) {
        // 특정 가게의 정보 조회
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("식당이 조회되지 않습니다."));

        // 구매한 식권 및 사용한 식권 개수를 계산
        int totalPurchasedTickets = ticketHistoryPurchaseRepository.getTotalPurchasedAmountByUserId(userId);
        int totalUsedTickets = ticketHistoryUsageRepository.getTotalUsedAmountByUserId(userId);

        // 남은 식권 개수 계산
        int remainingTickets = totalPurchasedTickets - totalUsedTickets;

        // 응답 데이터를 Dto에 담아서 반환
        return new TicketResponseDto(store.getName(), remainingTickets);
    }
}
