package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.ticket.model.TicketHistorySell;

public interface AdminTicketHistorySellRepository extends JpaRepository<TicketHistorySell, Long> {
}
