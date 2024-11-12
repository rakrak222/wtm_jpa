package org.wtm.web.common.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wtm.web.store.model.Notice;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
    Slice<Notice> findByStoreIdOrderByRegDateDesc(Long storeId, Pageable pageable);
}
