package org.wtm.web.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtm.web.ticket.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface AdminTicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByStoreId(Long storeId);

    Optional<Ticket> findTicketByStoreIdAndId(Long storeId, Long ticketId);

}
