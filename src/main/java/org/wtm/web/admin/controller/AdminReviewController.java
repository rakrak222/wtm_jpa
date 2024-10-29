package org.wtm.web.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.admin.dto.review.ReviewCommentCreateDto;
import org.wtm.web.admin.dto.review.ReviewCommentResponseDto;
import org.wtm.web.admin.dto.review.ReviewListDto;
import org.wtm.web.admin.service.AdminReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/stores/{storeId}/reviews/")
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    @GetMapping("/")
    public List<ReviewListDto> getReviews(@PathVariable Long storeId) {
        return adminReviewService.getReviewsByStoreId(storeId);
    }

    @PostMapping("/{reviewId}")
    public ResponseEntity<?> createReview(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @RequestBody ReviewCommentCreateDto reviewCommentCreateDto) {

        try {
            ReviewCommentResponseDto responseDto = adminReviewService.createReviewComment(storeId, reviewId, reviewCommentCreateDto);
            return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}