package org.wtm.web.review.mapper;

import org.wtm.web.review.dto.ReviewScaleAverageDto;
import org.wtm.web.review.dto.ReviewStatsDto;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewStatsMapper {

    public static ReviewStatsDto toDto(Double overallAverageScore, List<Object[]> scaleAverages) {

        // 각 ReviewScale의 평균 점수를 저장할 리스트 생성
        List<ReviewScaleAverageDto> reviewScaleAverages = scaleAverages.stream()
                .map(result -> ReviewScaleAverageDto.builder()
                        .scaleName((String) result[0])
                        .averageScore((Double) result[1])
                        .build())
                .collect(Collectors.toList());

        // 최종적으로 ReviewStatsDto 객체를 반환
        return ReviewStatsDto.builder()
                .overallAverageScore(overallAverageScore)
                .reviewScaleAverages(reviewScaleAverages)
                .build();
    }

}
