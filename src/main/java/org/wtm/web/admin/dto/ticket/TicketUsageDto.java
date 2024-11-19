package org.wtm.web.admin.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketUsageDto {
    private Long userId;
    private Long storeId;
    private Long ticketId;
    private Long ticketQuantity;
    private String type;
}
