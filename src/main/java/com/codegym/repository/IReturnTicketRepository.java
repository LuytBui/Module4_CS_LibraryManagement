package com.codegym.repository;


import com.codegym.model.ticket.ReturnTicket;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReturnTicketRepository extends PagingAndSortingRepository<ReturnTicket, Long> {
    Optional<ReturnTicket> findByBorrowTicket_Id(Long borrowTicketId);
    @Query(value = "select * from return_tickets where is_reviewed = false ", nativeQuery = true)
    List<ReturnTicket> findAllReturnTicketsNotReviewed ();
}
