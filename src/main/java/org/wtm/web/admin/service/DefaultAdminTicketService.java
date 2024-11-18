package org.wtm.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtm.web.admin.dto.ticket.*;
import org.wtm.web.admin.mapper.AdminTIcketMapper;
import org.wtm.web.admin.repository.*;
import org.wtm.web.store.model.Store;
import org.wtm.web.ticket.model.Ticket;
import org.wtm.web.ticket.model.TicketHistoryPurchase;
import org.wtm.web.ticket.model.TicketHistorySell;
import org.wtm.web.ticket.model.TicketHistoryUsage;
import org.wtm.web.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultAdminTicketService implements AdminTicketService {

    private final AdminTicketRepository ticketRepository;
    private final AdminTIcketMapper adminTicketMapper;
    private final AdminStoreRepository storeRepository;
    private final AdminUserRepository userRepository;
    private final AdminTicketHistoryPurchaseRepository historyPurchaseRepository;
    private final AdminTicketHistoryUsageRepository historyUsageRepository;
    private final AdminTicketHistorySellRepository historySellRepository;

    @Override
    @Transactional
    public List<TicketDto> getTicketsByStoreId(Long storeId) {
        List<Ticket> tickets = ticketRepository.findAllByStoreId(storeId);
        return tickets.stream()
                .map(adminTicketMapper:: toTicketListDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TicketResponseDto createTicket(Long storeId, TicketDto ticketDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("스토어를 찾을 수 없습니다."));
        Ticket ticket = adminTicketMapper.toTicketEntity(store, ticketDto);
        Ticket savedTicket = ticketRepository.save(ticket);
        return adminTicketMapper.toTicketResponseDto(savedTicket);
    }

    @Override
    @Transactional
    public TicketResponseDto updateTicket(Long storeId, Long ticketId, TicketDto ticketDto) {
        Ticket ticket = ticketRepository.findTicketByStoreIdAndId(storeId, ticketId)
                .orElseThrow(() -> new IllegalArgumentException("티켓을 찾을 수 없습니다."));
        adminTicketMapper.updateTicketFromDto(ticketDto, ticket);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return adminTicketMapper.toTicketResponseDto(updatedTicket);
    }

    @Override
    @Transactional
    public void deleteTicket(Long storeId, Long ticketId) {
        Ticket ticket = ticketRepository.findTicketByStoreIdAndId(storeId, ticketId)
                .orElseThrow(() -> new IllegalArgumentException("티켓을 찾을 수 없습니다."));
        ticketRepository.delete(ticket);
    }

    @Override
    @Transactional
    public TicketHistoryResponseDto createTicketUsageHistory(TicketUsageDto ticketUsageDto) {
        Integer purchasedTicketAmount =
                historyPurchaseRepository.getPurchaseAmountByUserId(ticketUsageDto.getUserId());
        User user = userRepository.findById(ticketUsageDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Ticket ticket = ticketRepository.findById(ticketUsageDto.getTicketId()).orElseThrow(() -> new IllegalArgumentException("티켓 정보를 찾을 수 없습니다."));
        Long amount = ticketUsageDto.getTicketQuantity();
        if(purchasedTicketAmount != null && purchasedTicketAmount > 0){
            TicketHistoryUsage ticketHistoryUsage = TicketHistoryUsage.builder()
                    .user(user)
                    .ticket(ticket)
                    .amount(amount)
                    .build();

            // 기존 엔티티가 존재하지 않을 경우에만 저장
            if (ticketHistoryUsage.getId() == null) {
                historyUsageRepository.save(ticketHistoryUsage);
                return TicketHistoryResponseDto.builder()
                        .userName(user.getName())
                        .ticketQuantity(amount)
                        .ticketPrice(ticket.getPrice())
                        .type(ticketUsageDto.getType())
                        .build();
            }else {
                System.out.println(ticketHistoryUsage.getId() + "는 이미 존재합니다.");
            }
        }
        System.out.println("사용할 티켓이 존재하지 않습니다");
        return null;
    }

    @Override
    @Transactional
    public TicketHistoryResponseDto createTicketPurchaseHistory(TicketPurchaseDto ticketPurchaseDto) {
        User user = userRepository.findById(ticketPurchaseDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Ticket ticket = ticketRepository.findById(ticketPurchaseDto.getTicketId())
                .orElseThrow(() -> new IllegalArgumentException("티켓을 찾을 수 없습니다."));
        Long amount = ticketPurchaseDto.getTicketQuantity();


        TicketHistorySell ticketHistorySell = TicketHistorySell.builder()
                .user(user)
                .ticket(ticket)
                .amount(amount)
                .build();
        historySellRepository.save(ticketHistorySell);

        TicketHistoryPurchase ticketHistoryPurchase = TicketHistoryPurchase.builder()
                .user(user)
                .ticket(ticket)
                .amount(amount)
                .build();
        historyPurchaseRepository.save(ticketHistoryPurchase);

        return TicketHistoryResponseDto.builder()
                .userName(user.getName())
                .ticketQuantity(amount)
                .ticketPrice(ticket.getPrice())
                .type(ticketPurchaseDto.getType())
                .build();
    }
}
