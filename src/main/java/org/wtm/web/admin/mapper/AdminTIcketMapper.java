package org.wtm.web.admin.mapper;


import org.springframework.stereotype.Component;
import org.wtm.web.admin.dto.ticket.TicketDto;
import org.wtm.web.admin.dto.ticket.TicketResponseDto;
import org.wtm.web.store.model.Store;
import org.wtm.web.ticket.model.Ticket;

@Component
public class AdminTIcketMapper {
    public TicketDto toTicketListDto(Ticket ticket) {
        return new TicketDto(
                ticket.getId(),
                ticket.getStore().getId(),
                ticket.getPrice(),
                ticket.getName(),
                ticket.getCategory()
        );
    }

    public Ticket toTicketEntity(Store store, TicketDto ticketDto) {
        return Ticket.builder()
                .store(store)
                .price(ticketDto.getPrice())
                .name(ticketDto.getName())
                .category(ticketDto.getCategoryId())
                .build();
    }

    public TicketResponseDto toTicketResponseDto(Ticket ticket){
        return new TicketResponseDto(
                ticket.getId(),
                ticket.getPrice(),
                ticket.getName(),
                ticket.getCategory()
        );
    }

    public void updateTicketFromDto (TicketDto ticketDto, Ticket ticket){
        ticket.updatePrice(ticketDto.getPrice());
        ticket.updateName(ticketDto.getName());
        ticket.updateCategory(ticketDto.getCategoryId());
    }
}
