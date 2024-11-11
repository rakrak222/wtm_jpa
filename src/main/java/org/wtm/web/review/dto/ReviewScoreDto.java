package org.wtm.web.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewScoreDto {
    private Long reviewScaleId;
    private Double reviewScore;
}
