package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.menu.model.Meal;
import org.wtm.web.menu.model.MenuImg;

import java.util.List;

public interface AdminMenuImgRepository extends JpaRepository<MenuImg, Long> {

    void deleteAllByMeal(Meal meal);

    List<MenuImg> findAllByMeal(Meal meal);
}
