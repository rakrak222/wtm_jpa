package org.wtm.web.review.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewComment is a Querydsl query type for ReviewComment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewComment extends EntityPathBase<ReviewComment> {

    private static final long serialVersionUID = 206253262L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewComment reviewComment = new QReviewComment("reviewComment");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final QReview review;

    public final org.wtm.web.store.model.QStore store;

    public final org.wtm.web.user.model.QUser user;

    public QReviewComment(String variable) {
        this(ReviewComment.class, forVariable(variable), INITS);
    }

    public QReviewComment(Path<? extends ReviewComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewComment(PathMetadata metadata, PathInits inits) {
        this(ReviewComment.class, metadata, inits);
    }

    public QReviewComment(Class<? extends ReviewComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
        this.store = inits.isInitialized("store") ? new org.wtm.web.store.model.QStore(forProperty("store"), inits.get("store")) : null;
        this.user = inits.isInitialized("user") ? new org.wtm.web.user.model.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

