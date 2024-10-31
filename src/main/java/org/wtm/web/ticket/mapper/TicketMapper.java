package org.wtm.web.ticket.mapper;

import org.wtm.web.store.model.Store;
import org.wtm.web.ticket.dto.TicketResponseDto;

public class TicketMapper {
    public static TicketResponseDto toDto(Store store, int remainingTickets) {
        return TicketResponseDto.builder()
                .name(store.getName())
                .remainingTickets(remainingTickets)
                .build();
    }
}
