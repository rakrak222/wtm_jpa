package org.wtm.web.store.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreSns is a Querydsl query type for StoreSns
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreSns extends EntityPathBase<StoreSns> {

    private static final long serialVersionUID = -1398917397L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreSns storeSns = new QStoreSns("storeSns");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final QStore store;

    public final StringPath type = createString("type");

    public final StringPath url = createString("url");

    public QStoreSns(String variable) {
        this(StoreSns.class, forVariable(variable), INITS);
    }

    public QStoreSns(Path<? extends StoreSns> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreSns(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreSns(PathMetadata metadata, PathInits inits) {
        this(StoreSns.class, metadata, inits);
    }

    public QStoreSns(Class<? extends StoreSns> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new QStore(forProperty("store"), inits.get("store")) : null;
    }

}

