package org.wtm.web.admin.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewScoreDto {
    private Long id;
    private Double score;
}
