package org.wtm.web.review.mapper;

import org.springframework.stereotype.Component;
import org.wtm.web.admin.dto.review.ReviewDto;
import org.wtm.web.review.dto.ReviewCommentDto;
import org.wtm.web.review.dto.ReviewListDto;
import org.wtm.web.review.dto.ReviewScoreDto;
import org.wtm.web.review.model.Review;
import org.wtm.web.review.model.ReviewImg;
import org.wtm.web.review.model.ReviewScore;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReviewMapper {
    public ReviewListDto toReviewListDto(Review review) {
        return ReviewListDto.builder()
                .reviewId(review.getId())
                .reviewContent(review.getContent())
                .reviewImageUrls(review.getReviewImgs().stream()  // 이미지 URL 리스트 설정
                        .map(ReviewImg::getImg)
                        .collect(Collectors.toList()))
                .userName(review.getUser().getName())
                .userProfilePicture(review.getUser().getProfilePicture())
                .reviewComments(toReviewCommentDtoList(review))
                .reviewScore(calculateAverageScore(review))
                .reviewRegDate(review.getRegDate())
                .build();
    }

    private List<ReviewCommentDto> toReviewCommentDtoList(Review review) {
        return review.getReviewComments().stream()
                .map(comment -> ReviewCommentDto.builder()
                        .reviewId(review.getId()) // reviewId 설정
                        .commentId(comment.getId())
                        .commentContent(comment.getContent())
                        .adminName(comment.getUser().getName())
                        .adminProfilePicture(comment.getUser().getProfilePicture())
                        .commentRegDate(comment.getRegDate())
                        .build())
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
