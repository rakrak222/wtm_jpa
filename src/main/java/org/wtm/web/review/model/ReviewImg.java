package org.wtm.web.review.model;

import jakarta.persistence.*;
import lombok.*;
import org.wtm.web.common.entity.BaseTimeEntity;

@Entity
@Table(name = "REVIEW_IMG")
@Getter
@Builder
@ToString(exclude = {"review"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReviewImg extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_img_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    private String img;
}
