package org.wtm.web.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.review.dto.*;
import org.wtm.web.review.service.impl.DefaultReviewService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/{storeId}/review-count")
    public ResponseEntity<?> getReviewCount(@PathVariable("storeId") long storeId) {
        ReviewCountDto reviewCount = reviewService.getReviewCount(storeId);
        if (reviewCount == null) {
            return ResponseEntity.status(404).body("리뷰가 조회되지 않습니다.");
        }
        return ResponseEntity.ok(reviewCount);
    }




    @GetMapping("/{storeId}/reviews")
    public ResponseEntity<Slice<ReviewListDto>> getReviews(
            @PathVariable Long storeId,
            @RequestParam(defaultValue = "date") String sortOption,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {

            Long userId = 2L;
            Pageable pageable = PageRequest.of(page, size);
            Slice<ReviewListDto> reviews = reviewService.getReviewsByStoreId(storeId, sortOption, pageable, userId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




    @PostMapping("{storeId}/ticketHistoryUsage/{ticketHistoryUsageId}/reviews")
    public ResponseEntity<?> addReview(
            @PathVariable Long storeId,
            @PathVariable Long ticketHistoryUsageId,
            @RequestParam("revisit") boolean revisit,
            @RequestParam("reviewContent") String reviewContent,
            @RequestParam("reviewScoresDtos") String scoresJson,
            @RequestParam(value = "files", required = false) List<MultipartFile> files) {

        Long userId = 2L; // 테스트를 위해 userId를 1로 고정

        // JSON 문자열을 DTO로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        List<ReviewScoreDto> scores;
        try {
            scores = objectMapper.readValue(scoresJson, new TypeReference<List<ReviewScoreDto>>() {});
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("점수 데이터를 변환하는 중 오류가 발생했습니다.");
        }

        // ReviewRequestDto 객체 생성
        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(revisit, reviewContent, scores);

        // 리뷰 서비스 호출
        reviewService.addReview(storeId, reviewRequestDto, files, userId);
        return ResponseEntity.status(201).body(Map.of("message", "리뷰가 성공적으로 등록되었습니다."));
    }

    @PostMapping("{storeId}/reviews/{reviewId}/reviewLike")
    public ResponseEntity<?> addReviewLike(@PathVariable Long reviewId) {
        Long FIXED_USER_ID = 2L;
        reviewService.addReviewLike(reviewId, FIXED_USER_ID);
        return ResponseEntity.ok("리뷰 Like가 활성화 되었습니다.");
    }

    @DeleteMapping("{storeId}/reviews/{reviewId}/reviewLike")
    public ResponseEntity<?> removeReviewLike(@PathVariable Long reviewId) {
        Long FIXED_USER_ID = 2L;
        reviewService.removeReviewLike(reviewId, FIXED_USER_ID);
        return ResponseEntity.ok("리뷰 Like가 삭제 되었습니다.");
    }


}
