package com.codegym.service.ticket;

import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.user.User;
import com.codegym.repository.IBorrowTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BorrowTicketService implements IBorrowTicketService{
    @Autowired
    private IBorrowTicketRepository borrowTicketRepository;

    @Override
    public Iterable<BorrowTicket> findAll() {
        return borrowTicketRepository.findAll();
    }

    @Override
    public BorrowTicket save(BorrowTicket borrowTicket) {
        return borrowTicketRepository.save(borrowTicket);
    }

    @Override
    public void deleteById(Long id) {
        borrowTicketRepository.deleteById(id);
    }

    @Override
    public Optional<BorrowTicket> findById(Long id) {
        return borrowTicketRepository.findById(id);
    }

    @Override
    public Page<BorrowTicket> findAll(Pageable pageable) {
        return borrowTicketRepository.findAll(pageable);
    }

    @Override
    public Iterable<BorrowTicket> findBorrowTicketByCustomer(User customer) {
        return borrowTicketRepository.findBorrowTicketByCustomer(customer);
    }

    @Override
    public Page<BorrowTicket> findBorrowTicketNotReviewed(Pageable pageable) {
        return borrowTicketRepository.findBorrowTicketNotReviewed(pageable);
    }
}
