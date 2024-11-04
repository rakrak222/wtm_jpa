package org.wtm.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor // 기본 생성자 추가
public class ReviewRequestDto {
    private boolean revisit;
    private String reviewContent;
}
