package com.codegym.repository;

import com.codegym.model.book.Book;
import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.ticket.BorrowTicketDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBorrowTicketDetailRepository extends PagingAndSortingRepository<BorrowTicketDetail, Long> {
//    @Query(value = "SELECT book_id FROM borrow_ticket_details WHERE borrow_ticket_id = ?", nativeQuery = true)
    @Query(value = "SELECT bdt.book FROM BorrowTicketDetail bdt INNER JOIN bdt.book WHERE bdt.borrowTicket = ?1")
    List<Book> findAllBookByBorrowTicket(BorrowTicket borrowTicket);
}
