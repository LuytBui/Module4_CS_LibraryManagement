package com.codegym.service.ticket;

import com.codegym.model.Book;
import com.codegym.model.ticket.BorrowTicketDetail;
import com.codegym.repository.IBorrowTicketDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public List<Book> findAllBookByBorrowTicket(Long borrowTicketId) {
        return borrowTicketDetailRepository.findAllBookByBorrowTicket(borrowTicketId);
    }
}
