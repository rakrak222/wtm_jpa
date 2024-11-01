package org.wtm.web.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wtm.web.review.dto.*;
import org.wtm.web.review.service.impl.DefaultReviewService;

import java.util.ArrayList;
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


//    @PostMapping("{storeId}/reviews")
//    public ResponseEntity<?> addReview(@PathVariable Long storeId,
//                                       @RequestParam("revisit") boolean revisit, // 단일 값
//                                       @RequestParam("reviewContent") String reviewContent, // 단일 값
//                                       @RequestParam("scores") String scoresJson, // 점수 리스트를 JSON 문자열로 받음
//                                       @RequestParam(value = "files", required = false) List<MultipartFile> files) { // 이미지 파일
//
//
//        // scoresJson을 객체로 변환
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<ReviewScoreDto> scores;
//        try {
//            scores = objectMapper.readValue(scoresJson, new TypeReference<List<ReviewScoreDto>>() {});
//        } catch (JsonProcessingException e) {
//            e.printStackTrace(); // 오류 메시지를 콘솔에 출력하여 확인
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("점수 데이터를 변환하는 중 오류가 발생했습니다.");
//        }
//
//        // ReviewRequestDto 생성
//        ReviewRequestDto reviewRequestDto = new ReviewRequestDto(revisit, reviewContent);
//
//        Long userId = 1L; // 테스트를 위해 userId를 1로 고정
//        reviewService.addReview(storeId, reviewRequestDto, scores, files, userId);
//
//        return ResponseEntity.status(201).body("리뷰가 성공적으로 등록되었습니다.");
//    }

    @PostMapping("{storeId}/reviews")
    public ResponseEntity<?> addReview(@PathVariable Long storeId,
                                       @RequestPart("reviewRequestDto") ReviewRequestDto reviewRequestDto, // JSON으로 받음
                                       @RequestParam List<ReviewScoreDto> scores, // JSON으로 받음
                                       @RequestParam(value = "files", required = false) List<MultipartFile> files) {

        Long userId = 1L; // 테스트를 위해 userId를 1로 고정

        // Separate handling of scores and files if needed
        reviewService.addReview(storeId, reviewRequestDto, scores, files, userId);
        return ResponseEntity.status(201).body("리뷰가 성공적으로 등록되었습니다.");
    }


}
