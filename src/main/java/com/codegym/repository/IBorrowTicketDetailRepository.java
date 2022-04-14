package com.codegym.repository;

import com.codegym.model.Book;
import com.codegym.model.ticket.BorrowTicketDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBorrowTicketDetailRepository extends PagingAndSortingRepository<BorrowTicketDetail, Long> {
    @Query(value = "SELECT book_id FROM borrow_ticket_details WHERE borrow_ticket_id = ?", nativeQuery = true)
    List<Book> findAllBookByBorrowTicket(Long borrowTicketId);
}
