package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.menu.model.MenuCategory;

public interface AdminMenuCategoryRepository extends JpaRepository<MenuCategory, Long> {
}
