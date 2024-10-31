package org.wtm.web.menu.model;

import jakarta.persistence.*;
import lombok.*;
import org.wtm.web.common.entity.BaseTimeEntity;


@Entity
@Table(name = "MENU_IMG")
@Getter
@Builder
@ToString(exclude = {"meal"})
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MenuImg extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_img_id")
    private Long id;

    private String img;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id")
    private Meal meal;

}
