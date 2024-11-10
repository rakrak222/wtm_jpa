package org.wtm.web.review.mapper;

import org.springframework.stereotype.Component;
import org.wtm.web.admin.dto.review.ReviewDto;
import org.wtm.web.review.dto.ReviewCommentDto;
import org.wtm.web.review.dto.ReviewListDto;
import org.wtm.web.review.dto.ReviewScoreDto;
import org.wtm.web.review.model.Review;
import org.wtm.web.review.model.ReviewScore;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {
    public ReviewListDto toReviewListDto(Review review) {
        return new ReviewListDto(
                review.getId(),
                review.getContent(),
                review.getUser().getName(),
                review.getUser().getProfilePicture(),
                toReviewCommentDtoList(review),
                calculateAverageScore(review),
                review.getRegDate()
        );
    }

    private List<ReviewCommentDto> toReviewCommentDtoList(Review review) {
        return review.getReviewComments().stream()
                .map(comment -> new ReviewCommentDto(
                        review.getId(),  // reviewId 추가
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getName(),
                        comment.getUser().getProfilePicture()
                ))
                .collect(Collectors.toList());
    }

    private Double calculateAverageScore(Review review) {
        return review.getReviewScores().stream()
                .mapToDouble(ReviewScore::getScore)
                .average()
                .orElse(0.0);
    }


    public Review toEntity(String reviewContent, boolean revisit, Store store, User user) {
        return Review.builder()
                .content(reviewContent)
                .revisit(revisit)
                .store(store)
                .user(user)
                .build();

    }
}
