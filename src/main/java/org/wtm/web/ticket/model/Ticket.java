package org.wtm.web.ticket.model;

import jakarta.persistence.*;
import lombok.*;
import org.wtm.web.store.model.Store;
import org.wtm.web.common.entity.BaseTimeEntity;

@Entity
@Table(name = "TICKET")
@Getter
@Builder
@ToString(exclude = {"store"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Ticket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @Column
    private String name;

    @Column
    private Long category;

    @Column(nullable = false, precision = 10, scale = 2)
    private Long price;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    public Ticket updatePrice(Long price) {
        this.price = price;
        return this;
    }
}
