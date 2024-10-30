package org.wtm.web.store.model;

import jakarta.persistence.*;
import lombok.*;
import org.wtm.web.bookmark.model.Bookmark;
import org.wtm.web.common.entity.BaseTimeEntity;
import org.wtm.web.review.model.Review;
import org.wtm.web.ticket.model.Ticket;
import org.wtm.web.user.model.User;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "STORE")
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Store extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column
    private String address;

    @Column(length = 20)
    private String contact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private User user;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column
    private String img;

    @OneToMany(mappedBy = "store")
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "store")
    private List<Bookmark> bookmarks; // 북마크와의 관계

    @OneToMany(mappedBy = "store")
    private List<Review> reviews; // 리뷰와의 관계

    @OneToMany(mappedBy = "store")
    private List<StoreSns> storeSnsList; // StoreSns와의 관계
}
