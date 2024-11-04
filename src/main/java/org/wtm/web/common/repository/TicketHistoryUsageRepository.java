package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wtm.web.ticket.model.TicketHistoryUsage;

@Repository
public interface TicketHistoryUsageRepository extends JpaRepository<TicketHistoryUsage, Long> {
    @Query("SELECT COALESCE(SUM(thu.amount), 0) FROM TicketHistoryUsage thu WHERE thu.user.id = :userId")
    int getTotalUsedAmountByUserId(@Param("userId") Long userId);
}