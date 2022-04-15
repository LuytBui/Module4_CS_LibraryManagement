package com.codegym.service.ticket;

import com.codegym.model.book.Book;
import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.ticket.BorrowTicketDetail;
import com.codegym.service.IGeneralService;

import java.util.List;

public interface IBorrowTicketDetailService extends IGeneralService<BorrowTicketDetail> {
    List<Book> findAllBookByBorrowTicket(BorrowTicket borrowTicket);
}
