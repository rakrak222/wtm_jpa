package org.wtm.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
//@AllArgsConstructor
@Builder
public class ReviewListDto {
    private Long reviewId;
    private String reviewContent;
    private List<String> reviewImageUrls;  // 이미지 URL 리스트 필드 추가
    private String userName;
    private String userProfilePicture;
    private List<ReviewCommentDto> reviewComments;
    private Double reviewScore;
    private LocalDateTime reviewRegDate;
    private String relativeDate;
    private Long helpfulCount;
    private boolean liked;

    // 기본 생성자 추가
    public ReviewListDto() {
    }
    // 필요한 생성자 추가
    public ReviewListDto(Long reviewId, String reviewContent, List<String> reviewImageUrls, String userName, String userProfilePicture, List<ReviewCommentDto> reviewComments, Double reviewScore, LocalDateTime reviewRegDate, String relativeDate, Long helpfulCount, boolean liked) {
        this.reviewId = reviewId;
        this.reviewContent = reviewContent;
        this.reviewImageUrls = reviewImageUrls;
        this.userName = userName;
        this.userProfilePicture = userProfilePicture;
        this.reviewComments = reviewComments;
        this.reviewScore = reviewScore;
        this.reviewRegDate = reviewRegDate;
        this.relativeDate = relativeDate;  // 상대 날짜 초기화
        this.helpfulCount = helpfulCount;
        this.liked = liked;
    }
}
