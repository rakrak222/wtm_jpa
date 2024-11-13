package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.MenuImg;

import java.time.LocalDate;
import java.util.List;

public interface AdminMenuImgRepository extends JpaRepository<MenuImg, Long> {
    List<MenuImg> findByMeal(Meal meal);

    List<MenuImg> findByMealStoreIdAndMealMealDate(Long storeId, LocalDate mealDate);
}
