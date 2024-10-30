package org.wtm.web.review.service;

import org.wtm.web.review.dto.ReviewStatsDto;

public interface ReviewService {

    ReviewStatsDto getReviewStats(Long storeId);
}
