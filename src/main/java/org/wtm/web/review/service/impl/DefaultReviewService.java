package org.wtm.web.review.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtm.web.common.repository.ReviewRepository;
import org.wtm.web.review.dto.ReviewListDto;
import org.wtm.web.review.dto.ReviewStatsDto;
import org.wtm.web.review.mapper.ReviewMapper;
import org.wtm.web.review.mapper.ReviewStatsMapper;
import org.wtm.web.review.model.Review;
import org.wtm.web.review.service.ReviewService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultReviewService implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewStatsDto getReviewStats(Long storeId) {

        // 전체 평균 점수
        Double overallAverageScore = reviewRepository.findOverallAverageScoreByStoreId(storeId);

        // ReviewScale 별 평균 점수
        List<Object[]> scaleAverages = reviewRepository.findAverageScoreByReviewScaleAndStoreId(storeId);

        // Mapper를 사용하여 DTO 생성
        return ReviewStatsMapper.toDto(overallAverageScore, scaleAverages);

    }

    /**
     * 리뷰조회
     */
    @Transactional(readOnly = true)
    public List<ReviewListDto> getReviewsByStoreId(Long storeId) {
        List<Review> reviews = reviewRepository.findAllByStoreId(storeId);
        return reviews.stream()
                .map(reviewMapper::toReviewListDto)
                .collect(Collectors.toList());
    }


}
