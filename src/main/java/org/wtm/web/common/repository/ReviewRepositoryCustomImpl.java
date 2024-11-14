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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
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
    public Slice<ReviewListDto> findAllByStoreIdWithSorting(Long storeId, String sortOption, Pageable pageable) {
        QReview review = QReview.review;
        QReviewScore reviewScore = QReviewScore.reviewScore;
        QReviewComment reviewComment = QReviewComment.reviewComment;
        QReviewImg reviewImg = QReviewImg.reviewImg;

        // 평균 평점 계산
        NumberExpression<Double> avgScore = reviewScore.score.avg();

        // 메인 리뷰 쿼리 실행
        List<ReviewListDto> content = queryFactory
                .select(Projections.fields(ReviewListDto.class,
                        review.id.as("reviewId"),
                        review.content.as("reviewContent"),
                        review.user.name.as("userName"),
                        review.user.profilePicture.as("userProfilePicture"),
                        avgScore.as("reviewScore"),
                        review.regDate.as("reviewRegDate")
                ))
                .from(review)
                .leftJoin(review.reviewScores, reviewScore)
                .where(review.store.id.eq(storeId))
                .groupBy(review.id)
                .orderBy("rating".equals(sortOption) ? avgScore.desc() : review.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1) // 다음 페이지 여부 확인을 위해 +1
                .fetch();

        // 다음 페이지 여부 결정
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            content.remove(pageable.getPageSize());
            hasNext = true;
        }

        // 댓글 및 이미지 매핑
        Map<Long, List<ReviewCommentDto>> commentMap = getReviewCommentsMap(storeId);
        Map<Long, List<String>> imageMap = getReviewImagesMap(storeId);

        content.forEach(r -> {
            r.setReviewComments(commentMap.getOrDefault(r.getReviewId(), List.of()));
            r.setReviewImageUrls(imageMap.getOrDefault(r.getReviewId(), List.of()));
        });

        return new SliceImpl<>(content, pageable, hasNext);
    }

    // 댓글 맵핑 함수
    private Map<Long, List<ReviewCommentDto>> getReviewCommentsMap(Long storeId) {
        QReviewComment reviewComment = QReviewComment.reviewComment;

        List<ReviewCommentDto> comments = queryFactory
                .select(Projections.fields(ReviewCommentDto.class,
                        reviewComment.review.id.as("reviewId"),
                        reviewComment.id.as("commentId"),
                        reviewComment.content.as("commentContent"),
                        reviewComment.user.name.as("adminName"),
                        reviewComment.user.profilePicture.as("adminProfilePicture"),
                        reviewComment.regDate.as("commentRegDate")
                ))
                .from(reviewComment)
                .where(reviewComment.review.store.id.eq(storeId))
                .fetch();

        return comments.stream()
                .collect(Collectors.groupingBy(ReviewCommentDto::getReviewId));
    }

    // 이미지 맵핑 함수
    private Map<Long, List<String>> getReviewImagesMap(Long storeId) {
        QReviewImg reviewImg = QReviewImg.reviewImg;

        List<Tuple> images = queryFactory
                .select(reviewImg.review.id, reviewImg.img)
                .from(reviewImg)
                .where(reviewImg.review.store.id.eq(storeId))
                .fetch();

        return images.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(reviewImg.review.id),
                        Collectors.mapping(tuple -> tuple.get(reviewImg.img), Collectors.toList())
                ));
    }
}