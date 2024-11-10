package org.wtm.web.store.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStore is a Querydsl query type for Store
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStore extends EntityPathBase<Store> {

    private static final long serialVersionUID = -1842106547L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStore store = new QStore("store");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final StringPath address = createString("address");

    public final ListPath<org.wtm.web.bookmark.model.Bookmark, org.wtm.web.bookmark.model.QBookmark> bookmarks = this.<org.wtm.web.bookmark.model.Bookmark, org.wtm.web.bookmark.model.QBookmark>createList("bookmarks", org.wtm.web.bookmark.model.Bookmark.class, org.wtm.web.bookmark.model.QBookmark.class, PathInits.DIRECT2);

    public final TimePath<java.time.LocalTime> closeTime = createTime("closeTime", java.time.LocalTime.class);

    public final StringPath contact = createString("contact");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath img = createString("img");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    public final StringPath name = createString("name");

    public final TimePath<java.time.LocalTime> openTime = createTime("openTime", java.time.LocalTime.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final ListPath<org.wtm.web.review.model.Review, org.wtm.web.review.model.QReview> reviews = this.<org.wtm.web.review.model.Review, org.wtm.web.review.model.QReview>createList("reviews", org.wtm.web.review.model.Review.class, org.wtm.web.review.model.QReview.class, PathInits.DIRECT2);

    public final ListPath<StoreSns, QStoreSns> storeSnsList = this.<StoreSns, QStoreSns>createList("storeSnsList", StoreSns.class, QStoreSns.class, PathInits.DIRECT2);

    public final ListPath<org.wtm.web.ticket.model.Ticket, org.wtm.web.ticket.model.QTicket> tickets = this.<org.wtm.web.ticket.model.Ticket, org.wtm.web.ticket.model.QTicket>createList("tickets", org.wtm.web.ticket.model.Ticket.class, org.wtm.web.ticket.model.QTicket.class, PathInits.DIRECT2);

    public final org.wtm.web.user.model.QUser user;

    public QStore(String variable) {
        this(Store.class, forVariable(variable), INITS);
    }

    public QStore(Path<? extends Store> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStore(PathMetadata metadata, PathInits inits) {
        this(Store.class, metadata, inits);
    }

    public QStore(Class<? extends Store> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new org.wtm.web.user.model.QUser(forProperty("user")) : null;
    }

}

