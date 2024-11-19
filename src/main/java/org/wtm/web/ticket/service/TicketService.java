package org.wtm.web.ticket.service;


import org.wtm.web.ticket.dto.TicketResponseDto;

public interface TicketService {
    TicketResponseDto getTicketsByStoreId(Long storeId, String username);
}
