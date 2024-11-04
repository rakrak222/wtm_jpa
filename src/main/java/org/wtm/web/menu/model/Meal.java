package org.wtm.web.menu.model;

import jakarta.persistence.*;
import lombok.*;
import org.wtm.web.store.model.Store;
import org.wtm.web.common.entity.BaseTimeEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "MEAL",
        uniqueConstraints = {
                // store_id, meal_date의 조합이 고유하게 설정되어 식별자로 기능
                @UniqueConstraint(columnNames = {"store_id", "meal_date"})
        }
)
@Getter
@Builder
@ToString(exclude = "store")
@NoArgsConstructor//(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Meal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;


    @Column(name = "meal_date", nullable = false)
    private LocalDate mealDate;
  
    @OneToMany(mappedBy = "meal")
    private List<MenuImg> menuImg = new ArrayList<>();
}
