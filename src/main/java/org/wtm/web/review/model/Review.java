package org.wtm.web.review.model;

import jakarta.persistence.*;
import lombok.*;
import org.wtm.web.store.model.Store;
import org.wtm.web.common.entity.BaseTimeEntity;
import org.wtm.web.user.model.User;

import java.util.List;

@Entity
@Table(name = "REVIEW")
@Getter
@Builder
@ToString(exclude = {"store", "user", "reviewComments", "reviewScores"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false)
    private Boolean revisit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewComment> reviewComments;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewScore> reviewScores;


}
