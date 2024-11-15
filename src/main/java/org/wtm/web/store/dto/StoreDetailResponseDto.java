package org.wtm.web.store.dto;

import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.wtm.web.auth.dto.Address;
import org.wtm.web.ticket.dto.TicketDto;

import java.util.List;

@Data
@AllArgsConstructor
public class StoreDetailResponseDto {

    @Embedded
    private Address address;

    private String contact;
    private String operatingHours;
    private List<StoreSnsDto> storeSnsList;
    private List<TicketDto> ticketList;

}
