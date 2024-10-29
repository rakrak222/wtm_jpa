package org.wtm.web.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtm.web.admin.dto.review.ReviewCommentCreateDto;
import org.wtm.web.admin.dto.review.ReviewCommentResponseDto;
import org.wtm.web.admin.dto.review.ReviewListDto;
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
    public List<ReviewListDto> getReviewsByStoreId(Long storeId) {
        List<Review> reviews = reviewRepository.findAllByStoreId(storeId);
        return reviews.stream().map(reviewMapper::toReviewListDto).collect(Collectors.toList());
    }

    /**
     * 리뷰 답글 작성
     */
    public ReviewCommentResponseDto createReviewComment(Long storeId, Long reviewId, ReviewCommentCreateDto reviewCommentCreateDto) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalArgumentException("스토어를 찾을 수 없습니다."));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다"));

        User user = userRepository.findById(reviewCommentCreateDto.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("사용자를 찾을 수 없습ㄴ디ㅏ."));

        ReviewComment reviewComment = reviewCommentMapper.toReviewCommentEntity(reviewCommentCreateDto,store,review,user);
        ReviewComment savedReviewComment = reviewCommentRepository.save(reviewComment);

        return reviewCommentMapper.toReviewCommentResponseDto(savedReviewComment);
//        return new ReviewCommentResponseDto(
//                savedReviewComment.getId(),
//                savedReviewComment.getContent(),
//                user.getName(),
//                user.getProfilePicture()
//        );
    }

}
