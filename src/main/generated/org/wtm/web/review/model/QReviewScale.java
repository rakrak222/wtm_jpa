package org.wtm.web.review.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReviewScale is a Querydsl query type for ReviewScale
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewScale extends EntityPathBase<ReviewScale> {

    private static final long serialVersionUID = -168618119L;

    public static final QReviewScale reviewScale = new QReviewScale("reviewScale");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public QReviewScale(String variable) {
        super(ReviewScale.class, forVariable(variable));
    }

    public QReviewScale(Path<? extends ReviewScale> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReviewScale(PathMetadata metadata) {
        super(ReviewScale.class, metadata);
    }

}

