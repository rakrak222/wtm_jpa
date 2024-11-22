package org.wtm.web.user.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewDto {

    private Long reviewId;
    private String content;
    //    private String reviewImg;
    private Long storeId;
    private Long userId;
    private Boolean revisit;
    private LocalDateTime regDate;
}
