package org.wtm.web.common.repository;


import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import org.wtm.web.review.dto.ReviewCommentDto;
import org.wtm.web.review.dto.ReviewListDto;
import org.wtm.web.review.model.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<ReviewListDto> findAllByStoreIdWithSorting(Long storeId, String sortOption) {
        QReview review = QReview.review;
        QReviewScore reviewScore = QReviewScore.reviewScore;
        QReviewComment reviewComment = QReviewComment.reviewComment;
        QReviewImg reviewImg = QReviewImg.reviewImg;

        NumberTemplate<Double> avgScore = Expressions.numberTemplate(
                Double.class,
                "({0})",
                JPAExpressions
                        .select(reviewScore.score.avg())
                        .from(reviewScore)
                        .where(reviewScore.review.eq(review))
        );

        List<ReviewListDto> reviews = queryFactory
                .select(Projections.fields(ReviewListDto.class,
                        review.id.as("reviewId"),
                        review.content.as("reviewContent"),
                        review.user.name.as("userName"),
                        review.user.profilePicture.as("userProfilePicture"),
                        avgScore.as("reviewScore"),
                        review.regDate.as("reviewRegDate")
                ))
                .from(review)
                .where(review.store.id.eq(storeId))
                .groupBy(review.id)
                .orderBy("rating".equals(sortOption) ? avgScore.desc() : review.regDate.desc())
                .fetch();

        List<ReviewCommentDto> comments = queryFactory
                .select(Projections.fields(ReviewCommentDto.class,
                        reviewComment.review.id.as("reviewId"),
                        reviewComment.id.as("commentId"),
                        reviewComment.content.as("commentContent"),
                        reviewComment.user.name.as("adminName"),
                        reviewComment.user.profilePicture.as("adminProfilePicture")
                ))
                .from(reviewComment)
                .where(reviewComment.review.store.id.eq(storeId))
                .fetch();

        Map<Long, List<ReviewCommentDto>> commentMap = comments.stream()
                .collect(Collectors.groupingBy(ReviewCommentDto::getReviewId));

        // ReviewImg 테이블에서 이미지 URL 가져오기
        List<Tuple> images = queryFactory
                .select(reviewImg.review.id, reviewImg.img)
                .from(reviewImg)
                .where(reviewImg.review.store.id.eq(storeId))
                .fetch();

        Map<Long, List<String>> imageMap = images.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(reviewImg.review.id),
                        Collectors.mapping(tuple -> tuple.get(reviewImg.img), Collectors.toList())
                ));

        reviews.forEach(r -> {
            r.setReviewComments(commentMap.getOrDefault(r.getReviewId(), List.of()));
            r.setReviewImageUrls(imageMap.getOrDefault(r.getReviewId(), List.of()));  // 이미지 URL 설정
        });

        return reviews;
    }
}
