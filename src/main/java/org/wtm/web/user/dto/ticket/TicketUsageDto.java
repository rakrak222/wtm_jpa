package org.wtm.web.user.dto.ticket;

import lombok.Getter;

@Getter
public class TicketUsageDto {
    private Long userId;
    private Long storeId;
    private Long amount;
}
