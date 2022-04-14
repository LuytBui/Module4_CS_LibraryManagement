package com.codegym.controller.ticket;

import com.codegym.model.ticket.BorrowTicket;
import com.codegym.service.ticket.IBorrowTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/borrowtickets")
public class BorrowTicketController {
    @Autowired
    private IBorrowTicketService borrowTicketService;

    @GetMapping
    public ResponseEntity<Page<BorrowTicket>> findAllBorrowTickets(Pageable pageable) {
        return new ResponseEntity<>(borrowTicketService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowTicket> findBorrowedTicketById(@PathVariable Long id) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(id);
        if (!borrowTicketOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(borrowTicketOptional.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BorrowTicket> createBorrowTicket(@RequestBody BorrowTicket borrowTicket) {
        return new ResponseEntity<>(borrowTicketService.save(borrowTicket), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BorrowTicket> updateBorrowTicket(@PathVariable Long id, @RequestBody BorrowTicket newBorrowTicket) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(id);
        if (!borrowTicketOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(borrowTicketService.save(newBorrowTicket), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BorrowTicket> deleteBorrowTicket(@PathVariable Long id) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(id);
        if (!borrowTicketOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        borrowTicketService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptBorrowTicket(@PathVariable Long id) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(id);
        if (!borrowTicketOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BorrowTicket borrowTicket = borrowTicketOptional.get();
        borrowTicket.setReviewed(true);
        borrowTicket.setAccepted(true);
        borrowTicket.setBorrowDate(getCurrentTime());
        return new ResponseEntity<>(borrowTicket, HttpStatus.OK);
    }

    @PostMapping("/{id}/deny")
    public ResponseEntity<?> denyBorrowTicket(@PathVariable Long id) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(id);
        if (!borrowTicketOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        BorrowTicket borrowTicket = borrowTicketOptional.get();
        borrowTicket.setReviewed(true);
        borrowTicket.setAccepted(false);
        return new ResponseEntity<>(borrowTicket, HttpStatus.OK);
    }

    public String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return String.valueOf(now);
    }
}
