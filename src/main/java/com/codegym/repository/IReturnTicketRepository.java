package com.codegym.repository;

import com.codegym.model.ticket.ReturnTicket;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IReturnTicketRepository extends PagingAndSortingRepository<ReturnTicket,Long> {

}
