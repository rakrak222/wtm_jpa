package org.wtm.web.store.mapper;


import org.springframework.stereotype.Component;
import org.wtm.web.store.dto.StoreDetailResponseDto;
import org.wtm.web.store.model.Store;
import org.wtm.web.store.model.StoreSns;
import org.wtm.web.ticket.model.Ticket;
import org.wtm.web.ticket.dto.TicketDto;
import org.wtm.web.store.dto.StoreSnsDto;


import java.util.List;
import java.util.stream.Collectors;

@Component
public class StoreDetailMapper {
    public StoreDetailResponseDto toDto(Store store, List<StoreSns> storeSnsList, List<Ticket> ticketList){

        return new StoreDetailResponseDto(
                store.getAddress(),
                store.getContact(),
                store.getOpenTime() + " - " + store.getCloseTime(),
                storeSnsList.stream().map(StoreSnsDto::new).collect(Collectors.toList()),
                ticketList.stream().map(TicketDto::new).collect(Collectors.toList())
        );
    }
}
