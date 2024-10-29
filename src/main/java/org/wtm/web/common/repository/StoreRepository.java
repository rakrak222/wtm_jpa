package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wtm.web.store.model.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}
