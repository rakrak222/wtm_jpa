package org.wtm.web.review.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewStatsDto {
    private Double overallAverageScore;
    private List<ReviewScaleAverageDto> reviewScaleAverages;
}
