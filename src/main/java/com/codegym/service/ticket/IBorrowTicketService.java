package com.codegym.service.ticket;

import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.user.User;
import com.codegym.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IBorrowTicketService extends IGeneralService<BorrowTicket> {
    Page<BorrowTicket> findAll(Pageable pageable);

    Iterable<BorrowTicket> findBorrowTicketByCustomer(User customer);
}
