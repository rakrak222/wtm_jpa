package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wtm.web.ticket.model.TicketHistoryPurchase;
import org.wtm.web.ticket.model.TicketHistoryUsage;

import java.util.List;
import java.util.Optional;

public interface AdminTicketHistoryUsageRepository extends JpaRepository<TicketHistoryUsage, Long> {

    @Query("SELECT sum(thu.amount) FROM TicketHistoryUsage thu WHERE thu.purchase.id = :purchaseId")
    Optional<Long> getUsageAmountByPurchaseId(@Param("purchaseId") Long purchaseId);

    }
