package com.codegym.controller.ticket;

import com.codegym.model.auth.ErrorMessage;
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

    @PostMapping
    public ResponseEntity<?> saveReturnTicket(@RequestBody ReturnTicket returnTicket) {
        Long borrowTicketId = returnTicket.getBorrowTicket().getId();
        Optional<BorrowTicket> borrowTicket = borrowTicketService.findById(borrowTicketId);
        if (!borrowTicket.isPresent()) {
            ErrorMessage errorMessage = new ErrorMessage("Thẻ mượn không tồn tại");
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
        Optional<ReturnTicket> findReturnTicket = returnTicketService.findByBorrowTicketId(borrowTicketId);
        if (findReturnTicket.isPresent()) {
            ErrorMessage errorMessage = new ErrorMessage("Đã tạo thẻ trả");
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
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

