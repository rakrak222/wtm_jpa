package org.wtm.web.user.dto.ticket;

import lombok.Builder;
import lombok.Getter;
import org.wtm.web.store.model.Store;
import org.wtm.web.ticket.model.Ticket;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class TicketSummaryDto {
    private String storeClosetime;
    private String storeOpentime;
    private String storeName;
    private Long storeId;
    private Long ticketId;
    private Long ticketPrice;
    private Long totalPurchasedTickets;
    private Long totalUsedTickets;
    private Long ticketAmount;
    private Double reviewAverage;
    private Boolean isBookmarked;
    private String storeImgUrl;

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
                .storeName(store.getName())
                .storeOpentime(store.getOpenTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .storeClosetime(store.getCloseTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .ticketPrice(ticket.getPrice())
                .totalPurchasedTickets(totalPurchasedTickets)
                .totalUsedTickets(totalUsedTickets)
                .ticketAmount(ticketAmount)
                .reviewAverage(reviewAverage)
                .ticketId(ticket.getId())
                .storeImgUrl(ticket.getStore().getImg())
                .build();
    }

}
