package org.wtm.web.admin.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewCommentDto {
    private Long commentId;
    private String commentContent;
    private String adminName;
    private String adminProfilePicture;
}
