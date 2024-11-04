package org.wtm.web.ticket.dto;


import lombok.Data;
import org.wtm.web.ticket.model.Ticket;

@Data
public class TicketDto {

    private Long price;
    private String createdTime;

    public TicketDto(Ticket ticket) {
        this.price = ticket.getPrice();
        this.createdTime = (ticket.getRegDate() != null) ? ticket.getRegDate().toString() : "N/A";
    }
}
