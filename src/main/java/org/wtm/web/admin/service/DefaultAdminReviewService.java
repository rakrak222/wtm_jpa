package org.wtm.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtm.web.admin.dto.review.*;
import org.wtm.web.admin.mapper.AdminReviewCommentMapper;
import org.wtm.web.admin.mapper.AdminReviewMapper;
import org.wtm.web.admin.repository.AdminReviewCommentRepository;
import org.wtm.web.admin.repository.AdminReviewRepository;
import org.wtm.web.admin.repository.AdminStoreRepository;
import org.wtm.web.admin.repository.AdminUserRepository;
import org.wtm.web.review.model.Review;
import org.wtm.web.review.model.ReviewComment;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultAdminReviewService implements AdminReviewService {

    private final AdminReviewRepository reviewRepository;
    private final AdminStoreRepository storeRepository;
    private final AdminUserRepository userRepository;
    private final AdminReviewCommentRepository reviewCommentRepository;

    private final AdminReviewMapper reviewMapper;
    private final AdminReviewCommentMapper reviewCommentMapper;

    /**
     * 리뷰조회
     */
    @Transactional(readOnly = true)
    public ReviewPageResponse getReviewsByStoreId(Long storeId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findAllByStoreId(storeId, pageable);
        List<ReviewListDto> reviewList = reviewPage.stream()
                .map(reviewMapper::toReviewListDto)
                .collect(Collectors.toList());
        return ReviewPageResponse.builder()
                .reviews(reviewList)
                .currentPage(reviewPage.getNumber())
                .totalPages(reviewPage.getTotalPages())
                .totalItems(reviewPage.getTotalElements())
                .build();
    }

    /**
     * 리뷰 답글 작성
     */
    @Transactional
    public ReviewCommentResponseDto createReviewComment(Long storeId, Long reviewId, ReviewCommentCreateDto reviewCommentCreateDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("스토어를 찾을 수 없습니다."));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다"));

        User user = userRepository.findById(reviewCommentCreateDto.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        ReviewComment reviewComment = reviewCommentMapper.toReviewCommentEntity(reviewCommentCreateDto, store, review, user);
        ReviewComment savedReviewComment = reviewCommentRepository.save(reviewComment);
        return reviewCommentMapper.toReviewCommentResponseDto(savedReviewComment);
    }

    @Transactional
    public ReviewCommentResponseDto updateReviewComment(Long storeId, Long reviewId, Long commentId, ReviewCommentUpdateDto reviewCommentUpdateDto) {
        ReviewComment reviewComment = reviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("답글을 찾을 수 없습니다."));
        if (!reviewComment.getStore().getId().equals(storeId) || !reviewComment.getReview().getId().equals(reviewId)) {
            throw new IllegalArgumentException("스토어 또는 리뷰 Id가 일치하지 않습니다.");
        }
        reviewComment.changeReviewCommentContent(reviewCommentUpdateDto.getContent());
        ReviewComment updatedReviewComment = reviewCommentRepository.save(reviewComment);
        return reviewCommentMapper.toReviewCommentResponseDto(updatedReviewComment);
    }

    @Transactional
    public void deleteReviewComment(Long storeId, Long reviewId, Long commentId) {
        ReviewComment reviewComment = reviewCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("답글을 찾을 수 없습니다."));

        if (!reviewComment.getStore().getId().equals(storeId) || !reviewComment.getReview().getId().equals(reviewId)) {
            throw new IllegalArgumentException("스토어 또는 리뷰 Id가 일치하지 않습니다.");
        }

        reviewCommentRepository.delete(reviewComment);
    }

}
