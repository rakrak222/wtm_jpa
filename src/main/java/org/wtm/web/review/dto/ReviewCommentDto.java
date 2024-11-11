package org.wtm.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ReviewCommentDto {
    private Long commentId;
    private String commentContent;
    private String adminName;
    private String adminProfilePicture;
}
