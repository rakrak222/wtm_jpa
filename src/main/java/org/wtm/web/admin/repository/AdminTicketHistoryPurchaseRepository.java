package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wtm.web.ticket.model.TicketHistoryPurchase;

import java.util.List;

public interface AdminTicketHistoryPurchaseRepository extends JpaRepository<TicketHistoryPurchase, Long> {

    @Query("SELECT thp FROM TicketHistoryPurchase thp WHERE thp.user.id = :userId ORDER BY thp.regDate ASC")
    List<TicketHistoryPurchase> findByUserIdOrderByCreatedDateAsc(@Param("userId") Long userId);

}
