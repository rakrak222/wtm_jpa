package org.wtm.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class ReviewCommentDto {
    private Long reviewId;  // reviewId 필드 추가
    private Long commentId;
    private String commentContent;
    private String adminName;
    private String adminProfilePicture;

    // 기본 생성자 추가
    public ReviewCommentDto() {
    }
}
