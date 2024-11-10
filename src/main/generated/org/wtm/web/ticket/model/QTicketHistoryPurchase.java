package org.wtm.web.ticket.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTicketHistoryPurchase is a Querydsl query type for TicketHistoryPurchase
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTicketHistoryPurchase extends EntityPathBase<TicketHistoryPurchase> {

    private static final long serialVersionUID = 907060388L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTicketHistoryPurchase ticketHistoryPurchase = new QTicketHistoryPurchase("ticketHistoryPurchase");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final QTicket ticket;

    public final org.wtm.web.user.model.QUser user;

    public QTicketHistoryPurchase(String variable) {
        this(TicketHistoryPurchase.class, forVariable(variable), INITS);
    }

    public QTicketHistoryPurchase(Path<? extends TicketHistoryPurchase> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTicketHistoryPurchase(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTicketHistoryPurchase(PathMetadata metadata, PathInits inits) {
        this(TicketHistoryPurchase.class, metadata, inits);
    }

    public QTicketHistoryPurchase(Class<? extends TicketHistoryPurchase> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ticket = inits.isInitialized("ticket") ? new QTicket(forProperty("ticket"), inits.get("ticket")) : null;
        this.user = inits.isInitialized("user") ? new org.wtm.web.user.model.QUser(forProperty("user")) : null;
    }

}

