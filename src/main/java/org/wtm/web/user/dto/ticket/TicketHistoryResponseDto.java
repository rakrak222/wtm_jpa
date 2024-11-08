package org.wtm.web.user.dto.ticket;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class TicketHistoryResponseDto {
    private List<TicketAllHistoryDto> combinedHistory;
    private Long totalPurchasedPrice;
    private Long totalUsedPrice;
    private Long totalAmount;
}
