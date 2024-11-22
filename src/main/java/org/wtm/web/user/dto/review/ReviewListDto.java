package org.wtm.web.user.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ReviewListDto {
    private Long reviewId;
    private String reviewContent;
    private String userName;
    private String userProfilePicture;
    private String adminProfilePicture;
    private List<ReviewCommentDto> reviewComments;
    private List<String> reviewImgs;
    private Double reviewScore;
    private LocalDateTime reviewRegDate;
}
