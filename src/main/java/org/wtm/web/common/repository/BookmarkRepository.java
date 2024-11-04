package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wtm.web.bookmark.model.Bookmark;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Bookmark findOneByStoreIdAndUserId(Long id, Long userId);

    @EntityGraph(attributePaths = "store")
    List<Bookmark> findAllByUserId(Long userId);
}
