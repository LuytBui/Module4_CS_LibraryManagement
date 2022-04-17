package com.codegym.service.ticket;

import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.ticket.ReturnTicket;
import com.codegym.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IReturnTicketService extends IGeneralService<ReturnTicket> {
    Optional<ReturnTicket> findByBorrowTicketId(Long borrowTicketId);
    Page<ReturnTicket> findAllReturnTicketsNotReviewed (Pageable pageable);
}
