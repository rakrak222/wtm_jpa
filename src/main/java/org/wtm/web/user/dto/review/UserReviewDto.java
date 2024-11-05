package org.wtm.web.user.dto.review;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserReviewDto {
    private Long reviewId;
    private String content;
    private LocalDateTime regDate;
    private Double averageScore;
    private boolean isBookmarked;
    private String storeName; // Store의 추가 필드
    private String storeImageUrl;
}
