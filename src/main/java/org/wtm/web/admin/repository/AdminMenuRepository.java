package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.Menu;

import java.time.LocalDate;
import java.util.List;

public interface AdminMenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m FROM Menu m WHERE m.store.id = :storeId AND m.meal.mealDate = :mealDate")
    List<Menu> findByStoreIdAndMealDate(@Param("storeId") Long storeId, @Param("mealDate") LocalDate mealDate);

    List<Menu> findAllByMeal(Meal meal);

}
