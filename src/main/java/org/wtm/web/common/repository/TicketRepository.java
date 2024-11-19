package org.wtm.web.common.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.wtm.web.ticket.model.Ticket;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findOneByStoreId(Long storeId);
    List<Ticket> findByStoreId(Long storeId);
    // added by jwhuh 2024-11-07
    @Query("SELECT price FROM Ticket WHERE id = :ticketId")
    Long findPriceById(@Param("ticketId") Long ticketId);

    Optional<Ticket> findOneByStoreId(Long storeId, Sort sort);

    List<Ticket> findAllByStoreId(Long storeId);
}
