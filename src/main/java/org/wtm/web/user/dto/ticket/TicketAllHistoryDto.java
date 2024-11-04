package org.wtm.web.user.dto.ticket;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TicketAllHistoryDto {

    private Long id;
    private Long userId;
    private Long ticketId;
    private Integer amount;
    private String type;
    private LocalDateTime regDate;

}
