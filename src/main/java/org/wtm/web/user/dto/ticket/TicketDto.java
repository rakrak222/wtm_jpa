package org.wtm.web.user.dto.ticket;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class TicketDto {
    private Long ticketId;
    private Long storeId;
    private String storeName;
    private Boolean isBookmarked;
    private Long ticketAmount;
}
