package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.menu.model.MenuCategory;

import java.util.Optional;

public interface MenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
    Optional<MenuCategory> findByName(String name);
}
