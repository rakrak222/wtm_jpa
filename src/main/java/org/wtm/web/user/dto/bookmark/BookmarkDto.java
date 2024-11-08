package org.wtm.web.user.dto.bookmark;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class BookmarkDto {
    private LocalTime storeCloseTime;
    private LocalTime storeOpenTime;
    private String storeName;
    private Long storeId;
    private Long ticketId;
    private Long ticketPrice;
    private Double reviewAverage;
    private Boolean isBookmarked;
}
