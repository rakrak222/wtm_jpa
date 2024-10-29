package org.wtm.web.admin.service;

import org.wtm.web.admin.dto.review.ReviewCommentCreateDto;
import org.wtm.web.admin.dto.review.ReviewCommentResponseDto;
import org.wtm.web.admin.dto.review.ReviewCommentUpdateDto;
import org.wtm.web.admin.dto.review.ReviewListDto;

import java.util.List;

public interface AdminReviewService {

    /*
     * 리뷰조회
     */
    public List<ReviewListDto> getReviewsByStoreId(Long storeId);

    /*
     * 리뷰 답글 작성
     */
    public ReviewCommentResponseDto createReviewComment(Long storeId, Long reviewId, ReviewCommentCreateDto reviewCommentCreateDto);

    /*
     * 리뷰 수정
     */
    public ReviewCommentResponseDto updateReviewComment(Long storeId, Long reviewId, Long commentId, ReviewCommentUpdateDto reviewCommentUpdateDto);

    /*
     * 리뷰 삭제
     */
    public void deleteReviewComment(Long storeId, Long reviewId, Long commentId);

}
