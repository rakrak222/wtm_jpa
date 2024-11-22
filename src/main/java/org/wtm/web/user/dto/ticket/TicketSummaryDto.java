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
        String openTime = null;
        String closeTime = null;
        Long ticketPrice = null;
        if(store.getOpenTime()!=null && store.getCloseTime()!=null){
            openTime = store.getOpenTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            closeTime = store.getCloseTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        if(ticket.getPrice()!=null){
            ticketPrice = ticket.getPrice();
        }
        return TicketSummaryDto.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .storeOpentime(openTime)
                .storeClosetime(closeTime)
                .ticketPrice(ticketPrice)
                .totalPurchasedTickets(totalPurchasedTickets)
                .totalUsedTickets(totalUsedTickets)
                .ticketAmount(ticketAmount)
                .reviewAverage(reviewAverage)
                .ticketId(ticket.getId())
                .storeImgUrl(ticket.getStore().getImg())
                .build();
    }

}
