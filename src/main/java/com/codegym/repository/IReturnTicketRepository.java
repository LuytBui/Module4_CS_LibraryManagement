package com.codegym.repository;

import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.ticket.ReturnTicket;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IReturnTicketRepository extends PagingAndSortingRepository<ReturnTicket, Long> {
    Optional<ReturnTicket> findByBorrowTicket_Id(Long borrowTicketId);

}
