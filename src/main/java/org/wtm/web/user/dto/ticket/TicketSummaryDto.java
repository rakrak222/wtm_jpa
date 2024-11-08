package org.wtm.web.user.dto.ticket;

import lombok.Builder;
import lombok.Getter;
import org.wtm.web.store.model.Store;
import org.wtm.web.ticket.model.Ticket;

import java.time.LocalTime;

@Getter
@Builder
public class TicketSummaryDto {
    private LocalTime storeClosetime;
    private LocalTime storeOpentime;
    private String stroreName;
    private Long storeId;
    private Long ticketId;
    private Long ticketPrice;
    private Long totalPurchasedTickets;
    private Long totalUsedTickets;
    private Long ticketAmount;
    private Double reviewAverage;
    private Boolean isBookmarked;

    public void addPurchasedTickets(Long amount) {
        this.totalPurchasedTickets += amount;
        this.ticketAmount += amount;
    }

    public void addUsedTickets(Long amount) {
        this.totalUsedTickets += amount;
        this.ticketAmount -= amount;
    }

    public void checkBookmark(boolean isBookmarked){
        this.isBookmarked = isBookmarked;
    }

    public void addReviewAverage(Double reviewAverage){
        this.reviewAverage = reviewAverage;
    }

    public static TicketSummaryDto form(
            Ticket ticket,
            Store store,
            Long totalPurchasedTickets,
            Long totalUsedTickets,
            Long ticketAmount,
            Double reviewAverage
            ) {
        return TicketSummaryDto.builder()
                .storeId(store.getId())
                .stroreName(store.getName())
                .storeOpentime(store.getOpenTime())
                .storeClosetime(store.getCloseTime())
                .ticketPrice(ticket.getPrice())
                .totalPurchasedTickets(totalPurchasedTickets)
                .totalUsedTickets(totalUsedTickets)
                .ticketAmount(ticketAmount)
                .reviewAverage(reviewAverage)
                .ticketId(ticket.getId())
                .build();
    }

}
