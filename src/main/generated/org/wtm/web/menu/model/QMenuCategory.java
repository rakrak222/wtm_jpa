package org.wtm.web.menu.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMenuCategory is a Querydsl query type for MenuCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMenuCategory extends EntityPathBase<MenuCategory> {

    private static final long serialVersionUID = 650628047L;

    public static final QMenuCategory menuCategory = new QMenuCategory("menuCategory");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath name = createString("name");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public QMenuCategory(String variable) {
        super(MenuCategory.class, forVariable(variable));
    }

    public QMenuCategory(Path<? extends MenuCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMenuCategory(PathMetadata metadata) {
        super(MenuCategory.class, metadata);
    }

}

