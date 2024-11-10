package org.wtm.web.menu.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMeal is a Querydsl query type for Meal
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMeal extends EntityPathBase<Meal> {

    private static final long serialVersionUID = -1095439851L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMeal meal = new QMeal("meal");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DatePath<java.time.LocalDate> mealDate = createDate("mealDate", java.time.LocalDate.class);

    public final ListPath<MenuImg, QMenuImg> menuImg = this.<MenuImg, QMenuImg>createList("menuImg", MenuImg.class, QMenuImg.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final org.wtm.web.store.model.QStore store;

    public QMeal(String variable) {
        this(Meal.class, forVariable(variable), INITS);
    }

    public QMeal(Path<? extends Meal> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMeal(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMeal(PathMetadata metadata, PathInits inits) {
        this(Meal.class, metadata, inits);
    }

    public QMeal(Class<? extends Meal> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new org.wtm.web.store.model.QStore(forProperty("store"), inits.get("store")) : null;
    }

}

