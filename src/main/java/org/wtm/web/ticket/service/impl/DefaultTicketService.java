package org.wtm.web.ticket.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtm.web.common.repository.StoreRepository;
import org.wtm.web.common.repository.TicketHistoryPurchaseRepository;
import org.wtm.web.common.repository.TicketHistoryUsageRepository;
import org.wtm.web.common.repository.UserRepository;
import org.wtm.web.store.model.Store;
import org.wtm.web.ticket.dto.TicketResponseDto;
import org.wtm.web.ticket.mapper.TicketMapper;
import org.wtm.web.ticket.service.TicketService;
import org.wtm.web.user.model.User;

@Service
@RequiredArgsConstructor
public class DefaultTicketService implements TicketService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final TicketHistoryPurchaseRepository ticketHistoryPurchaseRepository;
    private final TicketHistoryUsageRepository ticketHistoryUsageRepository;

    @Transactional
    public TicketResponseDto getTicketsByStoreId(Long storeId, String username) {
        // 특정 가게의 정보 조회

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("사용자가 조회되지 않습니다."));
        System.out.println("user.getId() = " + user.getId());
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("식당이 조회되지 않습니다."));

        // 구매한 식권 및 사용한 식권 개수를 계산
        int totalPurchasedTickets = ticketHistoryPurchaseRepository.getTotalPurchasedAmountByUserId(user.getId());
        int totalUsedTickets = ticketHistoryUsageRepository.getTotalUsedAmountByUserId(user.getId());

        // 남은 식권 개수 계산
        int remainingTickets = totalPurchasedTickets - totalUsedTickets;

        // 응답 데이터를 Dto에 담아서 반환
        return TicketMapper.toDto(store, remainingTickets);
    }
}
