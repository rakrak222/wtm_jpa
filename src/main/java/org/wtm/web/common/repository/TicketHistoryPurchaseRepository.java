package org.wtm.web.common.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wtm.web.ticket.model.TicketHistoryPurchase;
import org.wtm.web.ticket.model.TicketHistoryUsage;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketHistoryPurchaseRepository extends JpaRepository<TicketHistoryPurchase, Long> {

    @Query("SELECT COALESCE(SUM(thp.amount), 0) FROM TicketHistoryPurchase thp WHERE thp.user.id = :userId")
    int getTotalPurchasedAmountByUserId(@Param("userId") Long userId);

    // added by jwhuh 2024-11-04
    List<TicketHistoryPurchase> findByUserId(Long id);

//    @Query("select sum(thp.amount) from TicketHistoryPurchase thp where thp.user.id =:userId")
//    Integer getPurchaseAmountByUserId(@Param("userId") Long userId);

    @Query("select sum(thp.amount) from TicketHistoryPurchase thp where thp.user.id =:userId and thp.ticket.id =:ticketId")
    Optional<Integer> getPurchaseAmountByUserIdAndTicketId(@Param("userId") Long userId, @Param("ticketId")Long ticketId);

    List<TicketHistoryPurchase> findByUserIdAndRegDateBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);
    // 필터 사용 시 고려
    Page<TicketHistoryPurchase> findByUserIdAndRegDateBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime, Pageable pageable);

    @Query("select sum(thp.amount) from TicketHistoryPurchase thp where thp.user.id =:userId and thp.id =:id")
    Long countByTicketIdAndUserId(@Param("id")Long id, @Param("userId")Long userId);

    @Query("select thp from TicketHistoryPurchase thp where thp.user.id =:userId ORDER BY thp.regDate ASC")
    List<TicketHistoryPurchase> findAllByUserIdOrderByRegDateAsc(@Param("userId")Long userId);

    @Query("SELECT thp FROM TicketHistoryPurchase thp WHERE thp.user.id = :userId AND thp.ticket.id IN :ticketIds ORDER BY thp.regDate ASC")
    List<TicketHistoryPurchase> findAllByUserIdAndTicketIds(@Param("userId") Long userId, @Param("ticketIds") List<Long> ticketIds);

    @Query("SELECT SUM(thp.amount) FROM TicketHistoryPurchase thp WHERE thp.user.id = :userId")
    Optional<Long> getPurchaseAmountByUserId(@Param("userId") Long userId);
}