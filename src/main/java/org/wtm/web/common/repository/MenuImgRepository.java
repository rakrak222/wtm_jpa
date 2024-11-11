package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wtm.web.menu.model.MenuImg;

import java.util.List;

@Repository
public interface MenuImgRepository extends JpaRepository<MenuImg, Long> {
    List<MenuImg> findByMealId(Long mealId);
}
