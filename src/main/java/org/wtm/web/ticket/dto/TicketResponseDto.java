package org.wtm.web.ticket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TicketResponseDto {
    private String name; // 가게 이름
    private int remainingTickets; // 잔여 식권 개수
}
