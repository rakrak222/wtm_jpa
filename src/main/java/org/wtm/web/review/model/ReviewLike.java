package org.wtm.web.review.model;

import jakarta.persistence.*;
import lombok.*;
import org.wtm.web.common.entity.BaseTimeEntity;
import org.wtm.web.user.model.User;

@Entity
@Table(
        name = "REVIEW_LIKE",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "review_id"}
                )
        }
)
@Getter
@Builder
@ToString(exclude = {"user", "review"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;
}

