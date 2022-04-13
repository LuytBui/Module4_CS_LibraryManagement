package com.codegym.service.ticket;

import com.codegym.model.ticket.ReturnTicket;
import com.codegym.repository.IReturnTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReturnTicketService implements IReturnTicketService {
    @Autowired
    private IReturnTicketRepository returnTicketRepository;

    @Override
    public Iterable<ReturnTicket> findAll() {
        return returnTicketRepository.findAll();
    }

    @Override
    public ReturnTicket save(ReturnTicket returnTicket) {
        return returnTicketRepository.save(returnTicket);
    }

    @Override
    public void deleteById(Long id) {
        returnTicketRepository.deleteById(id);
    }


    @Override
    public Optional<ReturnTicket> findById(Long id) {
        return returnTicketRepository.findById(id);
    }

}
