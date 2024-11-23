package org.wtm.web.admin.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCommentCreateDto {
    private Long storeId;
    private Long userId;
    private Long reviewId;
    private String content;
    private String userName;
    private String userProfilePicture;
}
