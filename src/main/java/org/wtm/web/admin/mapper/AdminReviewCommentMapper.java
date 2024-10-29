package org.wtm.web.admin.mapper;


import org.springframework.stereotype.Component;
import org.wtm.web.admin.dto.review.ReviewCommentCreateDto;
import org.wtm.web.admin.dto.review.ReviewCommentResponseDto;
import org.wtm.web.review.model.Review;
import org.wtm.web.review.model.ReviewComment;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

@Component
public class AdminReviewCommentMapper {

    public ReviewComment toReviewCommentEntity (ReviewCommentCreateDto reviewCommentCreateDto, Store store, Review review, User user) {

        return ReviewComment.builder()
                .user(user)
                .store(store)
                .review(review)
                .content(reviewCommentCreateDto.getContent())
                .build();
    }

    public ReviewCommentResponseDto toReviewCommentResponseDto(ReviewComment reviewComment) {
        return new ReviewCommentResponseDto(
                reviewComment.getId(),
                reviewComment.getContent(),
                reviewComment.getUser().getName(),
                reviewComment.getUser().getProfilePicture()
        );
    }
}
