package org.wtm.web.admin.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryResponseDto {
    private String userName;
    private Long ticketQuantity;
    private Long ticketPrice;
    private String type;

}
