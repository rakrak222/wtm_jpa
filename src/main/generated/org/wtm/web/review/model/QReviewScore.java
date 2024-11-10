package org.wtm.web.review.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewScore is a Querydsl query type for ReviewScore
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewScore extends EntityPathBase<ReviewScore> {

    private static final long serialVersionUID = -168604479L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewScore reviewScore = new QReviewScore("reviewScore");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final QReview review;

    public final QReviewScale reviewScale;

    public final NumberPath<Double> score = createNumber("score", Double.class);

    public QReviewScore(String variable) {
        this(ReviewScore.class, forVariable(variable), INITS);
    }

    public QReviewScore(Path<? extends ReviewScore> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewScore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewScore(PathMetadata metadata, PathInits inits) {
        this(ReviewScore.class, metadata, inits);
    }

    public QReviewScore(Class<? extends ReviewScore> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
        this.reviewScale = inits.isInitialized("reviewScale") ? new QReviewScale(forProperty("reviewScale")) : null;
    }

}

