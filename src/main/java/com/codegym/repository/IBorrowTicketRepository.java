package com.codegym.repository;

import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBorrowTicketRepository extends PagingAndSortingRepository<BorrowTicket, Long> {
    Iterable<BorrowTicket> findAll();

    Iterable<BorrowTicket> findBorrowTicketByCustomer(User customer);

    @Query(value = "SELECT * FROM borrow_ticket WHERE is_reviewed = false", nativeQuery = true)
    Page<BorrowTicket> findBorrowTicketNotReviewed(Pageable pageable);
}
