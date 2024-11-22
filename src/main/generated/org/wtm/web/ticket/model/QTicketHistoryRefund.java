package org.wtm.web.ticket.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTicketHistoryRefund is a Querydsl query type for TicketHistoryRefund
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTicketHistoryRefund extends EntityPathBase<TicketHistoryRefund> {

    private static final long serialVersionUID = 34147291L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTicketHistoryRefund ticketHistoryRefund = new QTicketHistoryRefund("ticketHistoryRefund");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final StringPath status = createString("status");

    public final QTicket ticket;

    public final org.wtm.web.user.model.QUser user;

    public QTicketHistoryRefund(String variable) {
        this(TicketHistoryRefund.class, forVariable(variable), INITS);
    }

    public QTicketHistoryRefund(Path<? extends TicketHistoryRefund> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTicketHistoryRefund(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTicketHistoryRefund(PathMetadata metadata, PathInits inits) {
        this(TicketHistoryRefund.class, metadata, inits);
    }

    public QTicketHistoryRefund(Class<? extends TicketHistoryRefund> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ticket = inits.isInitialized("ticket") ? new QTicket(forProperty("ticket"), inits.get("ticket")) : null;
        this.user = inits.isInitialized("user") ? new org.wtm.web.user.model.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

