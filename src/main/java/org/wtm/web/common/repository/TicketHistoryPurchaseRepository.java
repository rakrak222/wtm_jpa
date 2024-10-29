package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wtm.web.ticket.model.TicketHistoryPurchase;

@Repository
public interface TicketHistoryPurchaseRepository extends JpaRepository<TicketHistoryPurchase, Long> {

    @Query("SELECT COALESCE(SUM(thp.amount), 0) FROM TicketHistoryPurchase thp WHERE thp.user.id = :userId")
    int getTotalPurchasedAmountByUserId(@Param("userId") Long userId);

}
