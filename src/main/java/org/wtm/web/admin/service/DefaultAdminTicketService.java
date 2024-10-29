package org.wtm.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtm.web.admin.dto.ticket.TicketDto;
import org.wtm.web.admin.dto.ticket.TicketResponseDto;
import org.wtm.web.admin.mapper.AdminTIcketMapper;
import org.wtm.web.admin.repository.AdminStoreRepository;
import org.wtm.web.admin.repository.AdminTicketRepository;
import org.wtm.web.store.model.Store;
import org.wtm.web.ticket.model.Ticket;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultAdminTicketService implements AdminTicketService {

    private final AdminTicketRepository ticketRepository;
    private final AdminTIcketMapper adminTicketMapper;
    private final AdminStoreRepository storeRepository;

    @Override
    public List<TicketDto> getTicketsByStoreId(Long storeId) {
        List<Ticket> tickets = ticketRepository.findAllByStoreId(storeId);
        return tickets.stream()
                .map(adminTicketMapper:: toTicketListDto)
                .collect(Collectors.toList());
    }

    @Override
    public TicketResponseDto createTicket(Long storeId, TicketDto ticketDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("스토어를 찾을 수 없습니다."));
        Ticket ticket = adminTicketMapper.toTicketEntity(store, ticketDto);
        Ticket savedTicket = ticketRepository.save(ticket);
        return adminTicketMapper.toTicketResponseDto(savedTicket);
    }

    @Override
    public TicketResponseDto updateTicket(Long storeId, Long ticketId, TicketDto ticketDto) {
        Ticket ticket = ticketRepository.findTicketByStoreIdAndId(storeId, ticketId)
                .orElseThrow(() -> new IllegalArgumentException("티켓을 찾을 수 없습니다."));
        adminTicketMapper.updateTicketFromDto(ticketDto, ticket);
        Ticket updatedTicket = ticketRepository.save(ticket);
        return adminTicketMapper.toTicketResponseDto(updatedTicket);
    }

    @Override
    public void deleteTicket(Long storeId, Long ticketId) {
        Ticket ticket = ticketRepository.findTicketByStoreIdAndId(storeId, ticketId)
                .orElseThrow(() -> new IllegalArgumentException("티켓을 찾을 수 없습니다."));
        ticketRepository.delete(ticket);
    }
}
