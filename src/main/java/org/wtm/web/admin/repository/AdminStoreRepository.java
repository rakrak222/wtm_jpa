package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wtm.web.store.model.Store;

@Repository
public interface AdminStoreRepository extends JpaRepository<Store, Long> {
}
