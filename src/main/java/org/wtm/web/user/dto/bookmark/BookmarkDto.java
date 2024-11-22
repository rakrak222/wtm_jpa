package org.wtm.web.user.dto.bookmark;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@Builder
public class BookmarkDto {
    private String username;
    private String storeCloseTime;
    private String storeOpenTime;
    private String storeName;
    private String storeImgUrl;
    private Long storeId;
    private Long ticketPrice;
    private Double reviewAverage;
    private Boolean isBookmarked;


}
