package org.wtm.web.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.wtm.web.ticket.model.TicketHistoryUsage;

@Repository
public interface TicketHistoryUsageRepository extends JpaRepository<TicketHistoryUsage, Long> {
}
