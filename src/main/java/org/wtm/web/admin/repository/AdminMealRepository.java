package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.store.model.Store;

import java.time.LocalDate;
import java.util.Optional;

public interface AdminMealRepository extends JpaRepository<Meal, Long> {
    Optional<Meal> findByStoreAndMealDate(Store store, LocalDate mealDate);
}
