package org.wtm.web.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wtm.web.admin.dto.review.*;

import java.util.List;

public interface AdminReviewService {

    /*
     * 리뷰조회
     */
    public ReviewPageResponse getReviewsByStoreId(Long storeId, Pageable pageable);

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
