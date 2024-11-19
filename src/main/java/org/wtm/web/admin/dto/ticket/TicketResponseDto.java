package org.wtm.web.admin.dto.ticket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketResponseDto {
    private Long id;
    private Long price;
    private String name;
    private Long categoryId;
}
