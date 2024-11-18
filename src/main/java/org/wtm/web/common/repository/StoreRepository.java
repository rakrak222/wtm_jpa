package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wtm.web.store.model.Store;
import org.wtm.web.store.model.StoreSns;
import org.wtm.web.ticket.model.Ticket;

import java.util.List;
import java.util.Optional;
import org.wtm.web.user.model.User;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom  {

    @Query("select s from Store s " +
            "join fetch s.user a ")
    List<Store> findAllWithDetails();

    @Query("SELECT s FROM Store s LEFT JOIN FETCH s.storeSnsList WHERE s.id = :id")
    Optional<Store> findStoreDetailsById(Long id);

    @Query("SELECT sns FROM StoreSns sns WHERE sns.store.id = :storeId")
    List<StoreSns> findStoreSnsById(@Param("storeId") Long storeId);

    @Query("SELECT t FROM Ticket t WHERE t.store.id = :storeId")
    List<Ticket> findTicketsByStoreId(@Param("storeId") Long storeId);


    @Query("SELECT COUNT(DISTINCT r), COALESCE(ROUND(AVG(rs.score), 1), 0) " +
            "FROM Review r LEFT JOIN r.reviewScores rs " +
            "WHERE r.store.id = :storeId")
    List<Object[]> findReviewStateByStoreId(@Param("storeId") Long storeId);

    // added by jwhuh 2024-11-04
    List<Store> findByIdIn(List<Long> storeIds);

    Optional<Store> findStoreByUser(User user);
}
