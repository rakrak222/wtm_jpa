package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wtm.web.bookmark.model.Bookmark;
import org.wtm.web.store.model.Store;
import org.wtm.web.user.model.User;

import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByUserAndStore(User user, Store store);

}
