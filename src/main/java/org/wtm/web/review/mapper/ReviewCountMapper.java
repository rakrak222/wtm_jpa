package org.wtm.web.review.mapper;

import org.springframework.stereotype.Component;
import org.wtm.web.review.dto.ReviewCountDto;

@Component
public class ReviewCountMapper {
    public static ReviewCountDto toDto(long reviewCount) {
        return ReviewCountDto.builder()
                .reviewCount(reviewCount)
                .build();

    }
}
