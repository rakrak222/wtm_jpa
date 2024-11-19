package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wtm.web.ticket.model.TicketHistoryPurchase;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketHistoryPurchaseRepository extends JpaRepository<TicketHistoryPurchase, Long> {

    @Query("SELECT COALESCE(SUM(thp.amount), 0) FROM TicketHistoryPurchase thp WHERE thp.user.id = :userId")
    int getTotalPurchasedAmountByUserId(@Param("userId") Long userId);

    // added by jwhuh 2024-11-04
    List<TicketHistoryPurchase> findByUserId(Long id);

    @Query("select sum(thp.amount) from TicketHistoryPurchase thp where thp.user.id =:userId")
    Integer getPurchaseAmountByUserId(@Param("userId") Long userId);

    List<TicketHistoryPurchase> findByUserIdAndRegDateBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);

}