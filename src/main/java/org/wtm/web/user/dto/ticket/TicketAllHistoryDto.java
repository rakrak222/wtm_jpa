package org.wtm.web.user.dto.ticket;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@Builder
public class TicketAllHistoryDto {

    private Long id;
    private Long userId;
    private Long ticketId;
    private Long amount;
    private String type;
    private LocalDateTime regDate;
    private String formattedRegDate;
    private String formattedTime;
    private String storeName;
    private Long price;
    private Boolean hasReview;

    // Builder에서 변환 작업 추가
    public static class TicketAllHistoryDtoBuilder {
        public TicketAllHistoryDtoBuilder regDate(LocalDateTime regDate) {
            this.regDate = regDate;

            LocalDateTime today = LocalDateTime.now();
            if (regDate.toLocalDate().isEqual(today.toLocalDate())) {
                this.formattedRegDate = "오늘";
            } else {
                this.formattedRegDate = regDate.format(DateTimeFormatter.ofPattern("dd일 E요일", Locale.KOREAN));
            }
            this.formattedTime = regDate.format(DateTimeFormatter.ofPattern("HH:mm"));
            return this;
        }
    }

}
