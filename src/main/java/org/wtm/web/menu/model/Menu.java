package org.wtm.web.menu.model;

import jakarta.persistence.*;
import lombok.*;
import org.wtm.web.store.model.Store;
import org.wtm.web.common.entity.BaseTimeEntity;
import org.wtm.web.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "MENU")
@Getter
@Builder
@ToString(exclude = {"meal", "store", "user", "category"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private MenuCategory category;

    public Menu(String name, MenuCategory category, Meal meal, Store store) {
        this.name = name;
        this.category = category;
        this.meal = meal;
        this.store = store;
        this.regDate = LocalDateTime.now();
        this.modDate = LocalDateTime.now();
    }
}
