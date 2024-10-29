package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.menu.model.Menu;

public interface AdminMenuRepository extends JpaRepository<Menu, Long> {
}
