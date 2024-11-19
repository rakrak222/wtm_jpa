package org.wtm.web.admin.service;

import org.wtm.web.admin.dto.ticket.*;

import java.util.List;

public interface AdminTicketService {

    public List<TicketDto> getTicketsByStoreId(Long storeId);

    public TicketResponseDto createTicket(Long storeId, TicketDto ticketDto);

    public TicketResponseDto updateTicket(Long storeId, Long ticketId, TicketDto ticketDto);

    public void deleteTicket(Long storeId, Long ticketId);

    public TicketHistoryResponseDto createTicketUsageHistory(TicketUsageDto ticketUsageDto);

    public TicketHistoryResponseDto createTicketPurchaseHistory(TicketPurchaseDto ticketPurchaseDto);
}
