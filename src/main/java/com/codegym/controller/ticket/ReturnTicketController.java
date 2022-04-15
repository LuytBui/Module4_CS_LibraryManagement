package com.codegym.controller.ticket;

import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.ticket.ReturnTicket;
import com.codegym.service.ticket.IBorrowTicketService;
import com.codegym.service.ticket.IReturnTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/returnTickets")
public class ReturnTicketController {
    @Autowired
    private IReturnTicketService returnTicketService;

    @Autowired
    private IBorrowTicketService borrowTicketService;

    @GetMapping
    public ResponseEntity<Iterable<ReturnTicket>> findAll() {
        Iterable<ReturnTicket> returnTickets = returnTicketService.findAll();
        return new ResponseEntity<>(returnTickets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReturnTicket> findById(@PathVariable Long id) {
        Optional<ReturnTicket> returnTicket = returnTicketService.findById(id);
        if (!returnTicket.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(returnTicket.get(), HttpStatus.OK);
    }

    @PostMapping("/save-return-for-borrow/{borrowTicketId}")
    public ResponseEntity<ReturnTicket> saveReturnTicket(@PathVariable Long borrowTicketId) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(borrowTicketId);
        if (!borrowTicketOptional.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        BorrowTicket borrowTicket = borrowTicketOptional.get();
        ReturnTicket returnTicket = new ReturnTicket(borrowTicket);

        borrowTicket.setHasReturnTicket(true);
        borrowTicketService.save(borrowTicket);

        return new ResponseEntity<>(returnTicketService.save(returnTicket), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReturnTicket> updateReturnTicket(@PathVariable Long id, @RequestBody ReturnTicket newReturnTicket) {
        Optional<ReturnTicket> returnTicket = returnTicketService.findById(id);
        if (!returnTicket.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        newReturnTicket.setId(returnTicket.get().getId());
        return new ResponseEntity<>(returnTicketService.save(newReturnTicket), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReturnTicket> deleteReturnTicket(@PathVariable Long id) {
        Optional<ReturnTicket> optionalReturnTicket = returnTicketService.findById(id);
        if (!optionalReturnTicket.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        returnTicketService.deleteById(id);
        return new ResponseEntity<>(optionalReturnTicket.get(), HttpStatus.OK);
    }
}

