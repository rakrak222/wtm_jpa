package org.wtm.web.review.service;

import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.review.dto.ReviewRequestDto;
import org.wtm.web.review.dto.ReviewScoreDto;
import org.wtm.web.review.dto.ReviewStatsDto;

import java.util.List;

public interface ReviewService {

    ReviewStatsDto getReviewStats(Long storeId);

    void addReview(Long storeId, ReviewRequestDto reviewRequestDto, List<ReviewScoreDto> scores, List<MultipartFile> files, Long userId);
}
