package org.wtm.web.review.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewCountDto {
    private long reviewCount;
}
