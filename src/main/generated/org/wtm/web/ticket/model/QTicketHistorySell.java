package org.wtm.web.ticket.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTicketHistorySell is a Querydsl query type for TicketHistorySell
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTicketHistorySell extends EntityPathBase<TicketHistorySell> {

    private static final long serialVersionUID = -737363851L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTicketHistorySell ticketHistorySell = new QTicketHistorySell("ticketHistorySell");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final QTicket ticket;

    public final org.wtm.web.user.model.QUser user;

    public QTicketHistorySell(String variable) {
        this(TicketHistorySell.class, forVariable(variable), INITS);
    }

    public QTicketHistorySell(Path<? extends TicketHistorySell> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTicketHistorySell(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTicketHistorySell(PathMetadata metadata, PathInits inits) {
        this(TicketHistorySell.class, metadata, inits);
    }

    public QTicketHistorySell(Class<? extends TicketHistorySell> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ticket = inits.isInitialized("ticket") ? new QTicket(forProperty("ticket"), inits.get("ticket")) : null;
        this.user = inits.isInitialized("user") ? new org.wtm.web.user.model.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

