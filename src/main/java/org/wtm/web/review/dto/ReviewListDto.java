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
}
