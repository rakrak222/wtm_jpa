package org.wtm.web.review.mapper;

import org.springframework.stereotype.Component;
import org.wtm.web.review.model.Review;
import org.wtm.web.review.model.ReviewScale;
import org.wtm.web.review.model.ReviewScore;

import java.util.Optional;

@Component
public class ReviewScoreMapper {
    public ReviewScore toEntity(Double reviewScore, ReviewScale reviewScale, Review review) {
        return ReviewScore.builder()
                .review(review)
                .score(reviewScore)
                .reviewScale(reviewScale)
                .build();

    }
}
