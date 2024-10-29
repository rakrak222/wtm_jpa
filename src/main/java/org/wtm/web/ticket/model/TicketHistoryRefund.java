package org.wtm.web.ticket.model;

import jakarta.persistence.*;
import lombok.*;
import org.wtm.web.common.entity.BaseTimeEntity;
import org.wtm.web.user.model.User;

@Entity
@Table(name = "TICKET_HISTORY_REFUND")
@Getter
@Builder
@ToString(exclude = {"user", "ticket"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TicketHistoryRefund extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_history_refund_id")
    private Long id;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private String status; // TODO: refund 상태값 관리할 상수 필요

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;
}
