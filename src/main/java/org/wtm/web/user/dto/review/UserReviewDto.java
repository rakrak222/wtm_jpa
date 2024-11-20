package org.wtm.web.user.dto.review;

import lombok.Builder;
import lombok.Getter;
import org.wtm.web.user.dto.ticket.TicketAllHistoryDto;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Getter
@Builder
public class UserReviewDto {
    private Long reviewId;
    private String content;
    private LocalDateTime regDate;
    private Double averageScore;
    private String storeName; // Store의 추가 필드
    private String reviewImgUrl;
    private String createdTime;


    // Builder에서 변환 작업 추가
    public static class UserReviewDtoBuilder {
        public UserReviewDto.UserReviewDtoBuilder regDate(LocalDateTime regDate) {
            this.regDate = regDate;
            LocalDateTime today = LocalDateTime.now();
            if (regDate.toLocalDate().isEqual(today.toLocalDate())) {
                long minutesDifference = Duration.between(regDate, today).toMinutes();
                if (minutesDifference > 60) {
                    long hoursDifference = minutesDifference / 60;
                    this.createdTime = hoursDifference + "시간 전";
                } else {
                    this.createdTime = minutesDifference + "분 전";
                }
            } else {
                long daysDifference = Duration.between(regDate.toLocalDate().atStartOfDay(), today.toLocalDate().atStartOfDay()).toDays();
                this.createdTime = daysDifference + "일 전";
            }
            return this;
        }
    }
}
