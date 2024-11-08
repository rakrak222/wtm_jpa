package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.store.model.StoreSns;

import java.util.List;
import java.util.Optional;

public interface AdminStoreSnsRepository extends JpaRepository<StoreSns, Long> {
    Optional<List<StoreSns>> findByStoreId(Long storeId);
}
