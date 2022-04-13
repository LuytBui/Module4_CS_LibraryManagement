package com.codegym.repository;

import com.codegym.model.ticket.BorrowTicket;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBorrowTicketRepository extends PagingAndSortingRepository<BorrowTicket, Long> {
}
