package org.wtm.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class ReviewListDto {
    private Long reviewId;
    private String reviewContent;
    private String userName;
    private String userProfilePicture;
    private List<ReviewCommentDto> reviewComments;
    private Double reviewScore;
    private LocalDateTime reviewRegDate;

    // 기본 생성자 추가
    public ReviewListDto() {
    }
    // 필요한 생성자 추가
    public ReviewListDto(Long reviewId, String reviewContent, String userName, String userProfilePicture, Double reviewScore, LocalDateTime reviewRegDate) {
        this.reviewId = reviewId;
        this.reviewContent = reviewContent;
        this.userName = userName;
        this.userProfilePicture = userProfilePicture;
        this.reviewScore = reviewScore;
        this.reviewRegDate = reviewRegDate;
    }
}
