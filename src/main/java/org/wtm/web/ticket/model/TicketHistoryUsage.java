package org.wtm.web.ticket.model;

import jakarta.persistence.*;
import lombok.*;
import org.wtm.web.common.entity.BaseTimeEntity;
import org.wtm.web.review.model.Review;
import org.wtm.web.user.model.User;

@Entity
@Table(name = "TICKET_HISTORY_USAGE")
@Getter
@Builder
@ToString(exclude = {"user", "ticket"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TicketHistoryUsage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_history_usage_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(nullable = false)
    private Long amount;

    @OneToOne(mappedBy = "ticketHistoryUsage", cascade = CascadeType.ALL, orphanRemoval = true)
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_history_purchase_id", nullable = true)
    private TicketHistoryPurchase purchase;
}
