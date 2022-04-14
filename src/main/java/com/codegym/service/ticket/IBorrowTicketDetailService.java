package com.codegym.service.ticket;

import com.codegym.model.Book;
import com.codegym.model.ticket.BorrowTicketDetail;
import com.codegym.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IBorrowTicketDetailService extends IGeneralService<BorrowTicketDetail> {
    List<Book> findAllBookByBorrowTicket(Long borrowTicketId);
}
