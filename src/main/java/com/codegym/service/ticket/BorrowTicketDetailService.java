package com.codegym.service.ticket;

import com.codegym.model.book.Book;
import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.ticket.BorrowTicketDetail;
import com.codegym.model.user.User;
import com.codegym.repository.IBorrowTicketDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BorrowTicketDetailService implements IBorrowTicketDetailService{
    @Autowired
    IBorrowTicketDetailRepository borrowTicketDetailRepository;

    @Override
    public Iterable<BorrowTicketDetail> findAll() {
        return null;
    }

    @Override
    public BorrowTicketDetail save(BorrowTicketDetail borrowTicketDetail) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<BorrowTicketDetail> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Book> findAllBookByBorrowTicket(BorrowTicket borrowTicket) {
        return borrowTicketDetailRepository.findAllBookByBorrowTicket(borrowTicket);
    }
}
