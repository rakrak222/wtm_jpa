package org.wtm.web.admin.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketDto {
    private Long id;
    private Long storeId;
    private Long price;
    private String name;
    private Long categoryId;
}
