package org.wtm.web.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtm.web.admin.dto.review.ReviewCommentCreateDto;
import org.wtm.web.admin.dto.review.ReviewCommentResponseDto;
import org.wtm.web.admin.dto.review.ReviewCommentUpdateDto;
import org.wtm.web.admin.dto.review.ReviewListDto;
import org.wtm.web.admin.service.AdminReviewService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    @GetMapping("/stores/{storeId}/reviews")
    public ResponseEntity<List<ReviewListDto>> getReviews(@PathVariable Long storeId){
        try {
            List<ReviewListDto> reviews = adminReviewService.getReviewsByStoreId(storeId);
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/stores/{storeId}/reviews/{reviewId}")
    public ResponseEntity<?> createReviewComment(
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

    @PutMapping("/stores/{storeId}/reviews/{reviewId}/comments/{commentId}")
    public ResponseEntity<?> updateReviewComment(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @PathVariable Long commentId,
            @RequestBody ReviewCommentUpdateDto reviewCommentUpdateDto
    ){
        try{
            ReviewCommentResponseDto responseDto = adminReviewService.updateReviewComment(storeId, reviewId, commentId, reviewCommentUpdateDto);
            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/stores/{storeId}/reviews/{reviewId}/comments/{commentId}")
    public ResponseEntity<?> deleteReviewComment(
            @PathVariable Long storeId,
            @PathVariable Long reviewId,
            @PathVariable Long commentId
    ) {
        try {
            adminReviewService.deleteReviewComment(storeId, reviewId, commentId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}