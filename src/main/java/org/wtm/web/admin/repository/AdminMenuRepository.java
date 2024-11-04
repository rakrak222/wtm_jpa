package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wtm.web.admin.dto.menu.MenuListDto;
import org.wtm.web.menu.model.Menu;

import java.util.List;

public interface AdminMenuRepository extends JpaRepository<Menu, Long> {
    //    @Query("select n from Notice n join fetch n.user u where n.store.id = :storeId")

    @Query("select m from Menu m " +
            "join fetch m.store s " +
            "where m.store.id = :storeId")
    List<Menu> findByStoreId(Long storeId);
}
