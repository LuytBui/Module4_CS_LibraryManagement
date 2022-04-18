package com.codegym.repository;


import com.codegym.model.ticket.ReturnTicket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReturnTicketRepository extends PagingAndSortingRepository<ReturnTicket, Long> {
    Optional<ReturnTicket> findByBorrowTicket_Id(Long borrowTicketId);

    @Query(value = "select * from return_tickets where is_reviewed = false ", nativeQuery = true)
    Page<ReturnTicket> findAllReturnTicketsNotReviewed (Pageable pageable);

    @Query(value = "select rt from ReturnTicket rt left join rt.borrowTicket left join  rt.borrowTicket.customer where rt.borrowTicket.customer.id = ?1")
    List<ReturnTicket> findAllByCustomer_Id(Long userId);
}
