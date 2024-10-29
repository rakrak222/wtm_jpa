package org.wtm.web.admin.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewCommentCreateDto {
    private Long storeId;
    private Long userId;
    private Long reviewId;
    private String content;
    private String userName;
    private String userProfilePicture;
}
