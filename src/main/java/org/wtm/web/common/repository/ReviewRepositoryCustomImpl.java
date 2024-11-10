package org.wtm.web.common.repository;


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
import org.wtm.web.review.model.QReview;
import org.wtm.web.review.model.QReviewComment;
import org.wtm.web.review.model.QReviewScore;
import org.wtm.web.review.model.Review;

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

        // 평균 점수를 계산
        NumberTemplate<Double> avgScore = Expressions.numberTemplate(
                Double.class,
                "({0})",
                JPAExpressions
                        .select(reviewScore.score.avg())
                        .from(reviewScore)
                        .where(reviewScore.review.eq(review))
        );

        // 리뷰 데이터 조회 (리뷰 댓글 제외)
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

        // 답글 데이터 조회
        List<ReviewCommentDto> comments = queryFactory
                .select(Projections.fields(ReviewCommentDto.class,
                        reviewComment.review.id.as("reviewId"),  // reviewId로 매핑할 수 있도록 추가
                        reviewComment.id.as("commentId"),
                        reviewComment.content.as("commentContent"),
                        reviewComment.user.name.as("adminName"),
                        reviewComment.user.profilePicture.as("adminProfilePicture")
                ))
                .from(reviewComment)
                .where(reviewComment.review.store.id.eq(storeId))
                .fetch();

        // reviewId를 기준으로 댓글을 매핑
        Map<Long, List<ReviewCommentDto>> commentMap = comments.stream()
                .collect(Collectors.groupingBy(ReviewCommentDto::getReviewId));

        // 각 리뷰에 대해 답글 리스트 설정
        reviews.forEach(r -> r.setReviewComments(commentMap.getOrDefault(r.getReviewId(), List.of())));

        return reviews;
    }
}
