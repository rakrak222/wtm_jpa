package org.wtm.web.ticket.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTicketHistoryUsage is a Querydsl query type for TicketHistoryUsage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTicketHistoryUsage extends EntityPathBase<TicketHistoryUsage> {

    private static final long serialVersionUID = -1381189410L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTicketHistoryUsage ticketHistoryUsage = new QTicketHistoryUsage("ticketHistoryUsage");

    public final org.wtm.web.common.entity.QBaseTimeEntity _super = new org.wtm.web.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Long> amount = createNumber("amount", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDate = _super.modDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final QTicket ticket;

    public final org.wtm.web.user.model.QUser user;

    public QTicketHistoryUsage(String variable) {
        this(TicketHistoryUsage.class, forVariable(variable), INITS);
    }

    public QTicketHistoryUsage(Path<? extends TicketHistoryUsage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTicketHistoryUsage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTicketHistoryUsage(PathMetadata metadata, PathInits inits) {
        this(TicketHistoryUsage.class, metadata, inits);
    }

    public QTicketHistoryUsage(Class<? extends TicketHistoryUsage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ticket = inits.isInitialized("ticket") ? new QTicket(forProperty("ticket"), inits.get("ticket")) : null;
        this.user = inits.isInitialized("user") ? new org.wtm.web.user.model.QUser(forProperty("user")) : null;
    }

}

