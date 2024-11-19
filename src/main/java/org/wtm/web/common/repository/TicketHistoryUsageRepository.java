package org.wtm.web.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wtm.web.ticket.model.TicketHistoryUsage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketHistoryUsageRepository extends JpaRepository<TicketHistoryUsage, Long> {
    @Query("SELECT COALESCE(SUM(thu.amount), 0) FROM TicketHistoryUsage thu WHERE thu.user.id = :userId")
    int getTotalUsedAmountByUserId(@Param("userId") Long userId);

    // added by jwhuh 2024-11-04
    List<TicketHistoryUsage> findByUserId(Long id);

    List<TicketHistoryUsage> findByUserIdAndRegDateBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    Page<TicketHistoryUsage> findByUserIdAndRegDateBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query("select sum(thu.amount) from TicketHistoryUsage thu where thu.user.id =:userId and thu.id =:id")
    Long countByTicketIdAndUserId(Long id, Long userId);

    @Query("SELECT SUM(thu.amount) FROM TicketHistoryUsage thu WHERE thu.user.id = :userId")
    Optional<Long> getUsageAmountByUserId(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(u.amount), 0) " +
            "FROM TicketHistoryUsage u " +
            "WHERE u.user.id = :userId AND u.ticket.id = :ticketId")
    Optional<Long> getUsageAmountByUserIdAndTicketId(@Param("userId") Long userId, @Param("ticketId") Long ticketId);

    @Query("SELECT SUM(thu.amount) FROM TicketHistoryUsage thu WHERE thu.user.id = :userId AND thu.ticket.id IN :ticketIds")
    Long countByTicketIdsAndUserId(@Param("ticketIds") List<Long> ticketIds, @Param("userId") Long userId);
}