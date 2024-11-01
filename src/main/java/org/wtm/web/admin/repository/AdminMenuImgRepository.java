package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.menu.model.MenuImg;

public interface AdminMenuImgRepository extends JpaRepository<MenuImg, Long> {
}
