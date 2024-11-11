package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wtm.web.menu.model.Meal;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    @Query("SELECT m FROM Meal m WHERE m.mealDate = :mealDate AND m.store.id = :storeId")
    Optional<Meal> findByMealDateAndStoreId(@Param("mealDate") LocalDate mealDate, @Param("storeId") Long storeId);

    Optional<Meal> findByStoreIdAndMealDate(Long storeId, LocalDate mealDate);
}
