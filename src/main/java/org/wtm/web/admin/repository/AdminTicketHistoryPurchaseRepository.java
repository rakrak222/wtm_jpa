package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wtm.web.ticket.model.TicketHistoryPurchase;

public interface AdminTicketHistoryPurchaseRepository extends JpaRepository<TicketHistoryPurchase, Long> {

    @Query("select sum(thp.amount) from TicketHistoryPurchase thp where thp.user.id =:userId")
    Integer getPurchaseAmountByUserId(@Param("userId") Long userId);
}
