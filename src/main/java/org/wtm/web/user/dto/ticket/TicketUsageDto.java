package org.wtm.web.user.dto.ticket;

import lombok.Getter;

@Getter
public class TicketUsageDto {
    private Long id;
    private Long userId;
    private Long storeId;
    private Long ticketId;
    private int amount;
}
