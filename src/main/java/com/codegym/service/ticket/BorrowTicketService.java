package com.codegym.service.ticket;

import com.codegym.model.ticket.BorrowTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BorrowTicketService implements IBorrowTicketService{
    @Autowired
    private IBorrowTicketService borrowTicketService;

    @Override
    public Iterable<BorrowTicket> findAll() {
        return null;
    }

    @Override
    public BorrowTicket save(BorrowTicket borrowTicket) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Optional<BorrowTicket> findById(Long id) {
        return Optional.empty();
    }
}
