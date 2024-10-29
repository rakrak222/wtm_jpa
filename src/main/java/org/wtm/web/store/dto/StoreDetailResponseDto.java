package org.wtm.web.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.wtm.web.ticket.dto.TicketDto;

import java.util.List;

@Data
@AllArgsConstructor
public class StoreDetailResponseDto {

    private String address;
    private String contact;
    private String operatingHours;
    private List<StoreSnsDto> storeSnsList;
    private List<TicketDto> ticketList;

}
