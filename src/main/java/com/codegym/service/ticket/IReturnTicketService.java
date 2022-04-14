package com.codegym.service.ticket;

import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.ticket.ReturnTicket;
import com.codegym.service.IGeneralService;

import java.util.Optional;

public interface IReturnTicketService extends IGeneralService<ReturnTicket> {
    Optional<ReturnTicket> findByBorrowTicketId(Long borrowTicketId);
}
