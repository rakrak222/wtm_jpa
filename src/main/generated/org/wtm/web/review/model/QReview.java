package org.wtm.web.review.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 458034257L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final ListPath<ReviewComment, QReviewComment> reviewComments = this.<ReviewComment, QReviewComment>createList("reviewComments", ReviewComment.class, QReviewComment.class, PathInits.DIRECT2);

    public final ListPath<ReviewImg, QReviewImg> reviewImages = this.<ReviewImg, QReviewImg>createList("reviewImages", ReviewImg.class, QReviewImg.class, PathInits.DIRECT2);

    public final ListPath<ReviewScore, QReviewScore> reviewScores = this.<ReviewScore, QReviewScore>createList("reviewScores", ReviewScore.class, QReviewScore.class, PathInits.DIRECT2);

    public final BooleanPath revisit = createBoolean("revisit");

    public final org.wtm.web.store.model.QStore store;

    public final org.wtm.web.ticket.model.QTicketHistoryUsage ticketHistoryUsage;

    public final org.wtm.web.user.model.QUser user;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new org.wtm.web.store.model.QStore(forProperty("store"), inits.get("store")) : null;
        this.ticketHistoryUsage = inits.isInitialized("ticketHistoryUsage") ? new org.wtm.web.ticket.model.QTicketHistoryUsage(forProperty("ticketHistoryUsage"), inits.get("ticketHistoryUsage")) : null;
        this.user = inits.isInitialized("user") ? new org.wtm.web.user.model.QUser(forProperty("user")) : null;
    }

}

