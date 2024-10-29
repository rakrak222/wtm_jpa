package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.store.model.Notice;

import java.util.List;
import java.util.Optional;

public interface AdminNoticeRepository extends JpaRepository<Notice, Long> {

//    @Query("select n from Notice n join fetch n.user u where n.store.id = :storeId")
//    List<Notice> findNoticesWithUserByStoreId(@Param("storeId") Long storeId);

    @EntityGraph(attributePaths = {"store", "user"})
    List<Notice> findNoticesWithUserByStoreId(Long storeId);

    Optional<Notice> findByStoreIdAndId(Long storeId, Long noticeId);
}
