package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.wtm.web.ticket.model.TicketHistoryPurchase;
import org.wtm.web.ticket.model.TicketHistoryUsage;

import java.util.List;
import java.util.Optional;

public interface AdminTicketHistoryUsageRepository extends JpaRepository<TicketHistoryUsage, Long> {



    @Query("select sum(thp.amount) from TicketHistoryUsage thp where thp.user.id =:userId and thp.ticket.id =:ticketId")
    Optional<Integer> getUsageAmountByUserIdAndTicketId(Long userId, Long ticketId);
}
