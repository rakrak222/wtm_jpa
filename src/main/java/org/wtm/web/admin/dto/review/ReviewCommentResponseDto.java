package org.wtm.web.admin.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewCommentResponseDto {
    private Long id;
    private String content;
    private String username;
    private String profilePicture;
}

