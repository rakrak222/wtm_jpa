package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wtm.web.ticket.model.TicketHistoryUsage;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketHistoryUsageRepository extends JpaRepository<TicketHistoryUsage, Long> {
    @Query("SELECT COALESCE(SUM(thu.amount), 0) FROM TicketHistoryUsage thu WHERE thu.user.id = :userId")
    int getTotalUsedAmountByUserId(@Param("userId") Long userId);

    // added by jwhuh 2024-11-04
    List<TicketHistoryUsage> findByUserId(Long id);

    List<TicketHistoryUsage> findByUserIdAndRegDateBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}