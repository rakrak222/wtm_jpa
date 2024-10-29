package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wtm.web.ticket.model.TicketHistoryPurchase;

@Repository
public interface TicketHistoryPurchaseRepository extends JpaRepository<TicketHistoryPurchase, Long> {
}
