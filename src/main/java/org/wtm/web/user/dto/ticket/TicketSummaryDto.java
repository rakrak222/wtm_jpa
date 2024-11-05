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
    private int totalPurchasedTickets = 0;
    private int totalUsedTickets = 0;
    private int ticketAmount;
    private double reviewAverage;
    private boolean bookmarked = false;

    public void addPurchasedTickets(int amount) {
        this.totalPurchasedTickets += amount;
        this.ticketAmount += amount;
    }

    public void addUsedTickets(int amount) {
        this.totalUsedTickets += amount;
        this.ticketAmount -= amount;
    }

    public void checkBookmark(boolean isBookmarked){
        this.bookmarked = isBookmarked;
    }

    public void addReviewAverage(double reviewAverage){
        this.reviewAverage = reviewAverage;
    }

    public static TicketSummaryDto form(
            Ticket ticket,
            Store store,
            int totalPurchasedTickets,
            int totalUsedTickets,
            int ticketAmount,
            double reviewAverage
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
