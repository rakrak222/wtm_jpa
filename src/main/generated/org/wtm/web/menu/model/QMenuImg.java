package org.wtm.web.menu.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMenuImg is a Querydsl query type for MenuImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMenuImg extends EntityPathBase<MenuImg> {

    private static final long serialVersionUID = -1074738606L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMenuImg menuImg = new QMenuImg("menuImg");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath img = createString("img");

    public final QMeal meal;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public QMenuImg(String variable) {
        this(MenuImg.class, forVariable(variable), INITS);
    }

    public QMenuImg(Path<? extends MenuImg> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMenuImg(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMenuImg(PathMetadata metadata, PathInits inits) {
        this(MenuImg.class, metadata, inits);
    }

    public QMenuImg(Class<? extends MenuImg> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.meal = inits.isInitialized("meal") ? new QMeal(forProperty("meal"), inits.get("meal")) : null;
    }

}

