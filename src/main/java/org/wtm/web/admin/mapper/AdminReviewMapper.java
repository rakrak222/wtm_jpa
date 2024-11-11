package org.wtm.web.admin.mapper;


import org.springframework.stereotype.Component;
import org.wtm.web.admin.dto.review.ReviewCommentDto;
import org.wtm.web.admin.dto.review.ReviewDto;
import org.wtm.web.admin.dto.review.ReviewListDto;
import org.wtm.web.review.model.Review;
import org.wtm.web.review.model.ReviewImg;
import org.wtm.web.review.model.ReviewScore;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AdminReviewMapper {

    public ReviewListDto toReviewListDto(Review review) {
        return new ReviewListDto(
                review.getId(),
                review.getContent(),
                review.getUser().getName(),
                review.getUser().getProfilePicture(),
                review.getStore().getUser().getProfilePicture(),
                toReviewCommentDtoList(review),
                review.getReviewImgs().stream()
                        .map(ReviewImg::getImg) // ReviewImg의 imageUrl만 추출
                        .filter(Objects::nonNull)
                        .toList(), // List<String>으로 변환
                calculateAverageScore(review),
                review.getRegDate()
        );
    }

    private List<ReviewCommentDto> toReviewCommentDtoList(Review review) {
        return review.getReviewComments().stream()
                .map(comment -> new ReviewCommentDto(
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

    public ReviewDto toReviewDto(Review review) {
        return new ReviewDto(
                review.getId(),
                review.getContent(),
                review.getStore().getId(),
                review.getUser().getId(),
                review.getRevisit(),
                review.getRegDate()
        );
    }
}
