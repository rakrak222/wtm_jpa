package org.wtm.web.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wtm.web.review.dto.ReviewListDto;
import org.wtm.web.review.dto.ReviewStatsDto;
import org.wtm.web.review.service.impl.DefaultReviewService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class ReviewController {

    private final DefaultReviewService reviewService;

    @GetMapping("/{storeId}/review-stats")
    public ResponseEntity<?> getReviewStats(@PathVariable("storeId") long storeId) {
        ReviewStatsDto reviewStats = reviewService.getReviewStats(storeId);
        if (reviewStats == null || (reviewStats.getOverallAverageScore() == 0 && reviewStats.getReviewScaleAverages().isEmpty())) {
            return ResponseEntity.status(404).body("리뷰 통계가 없습니다.");
        }
        return ResponseEntity.ok(reviewStats);
    }


    @GetMapping("/{storeId}/reviews")
    public ResponseEntity<List<ReviewListDto>> getReviews(@PathVariable Long storeId){
        try {
            List<ReviewListDto> reviews = reviewService.getReviewsByStoreId(storeId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }


}
