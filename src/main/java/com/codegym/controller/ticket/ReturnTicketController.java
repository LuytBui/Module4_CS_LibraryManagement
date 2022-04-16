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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

//    @GetMapping("/{id}/books")  // lay ra danh sach books cua the tra nay
//    public ResponseEntity<Iterable<Book>>
//@GetMapping("/{id/books}")
//public ResponseEntity<Iterable<Book>> findAllBooksByReturnTicketID(@PathVariable Long id, @RequestBody ReturnTicket returnTicket, @RequestBody BorrowTicketDetail){
//        Long bookId = borr
//        Long borrowTicketId = returnTicket.getBorrowTicket().getId();
//        Optional<ReturnTicket> returnTicketOptional = returnTicketService.findById(id);
//        if (!returnTicketOptional.isPresent()){
//            return new  ResponseEntity<>(HttpStatus.BAD_REQUEST);




    //    @PostMapping("/{id}/accept") // thu thu chap thuan the tra
    // setAccept(true)
    // set Date
    // set status: ReturnTicket.statuses
    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptReturnTicket(@PathVariable Long id) {
        Optional<ReturnTicket> returnTicketOptional = returnTicketService.findById(id);
        if (!returnTicketOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ReturnTicket returnTicket = returnTicketOptional.get();
        returnTicket.setReviewed(true);
        returnTicket.setAccepted(true);
        returnTicket.setReturnDate(getCurrentTime());
        return new ResponseEntity<>(returnTicket, HttpStatus.OK);
    }

//    @PostMapping("/{id}/deny") // thu thu tu choi the tra
    // setAccept(false)

    // setReviewed(true)
    // luu DB
    // tra ve new ResponseEntity<>(return ticket, H-OK)
    @PostMapping("/{id}/deny")
    public ResponseEntity<?> denyReturnTicket(@PathVariable Long id) {
        Optional<ReturnTicket> returnTicketOptional = returnTicketService.findById(id);
        if (!returnTicketOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ReturnTicket returnTicket = returnTicketOptional.get();
        returnTicket.setReviewed(true);
        returnTicket.setAccepted(false);
        return new ResponseEntity<>(returnTicket, HttpStatus.OK);
    }

    public String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return String.valueOf(now);
    }

    @GetMapping("/notReviewed")
    public ResponseEntity<List<ReturnTicket>> findAllReturnTicketNotReviewed() {
        List<ReturnTicket> returnTickets = returnTicketService.findAllReturnTicketNotReviewed();
        return new ResponseEntity<>(returnTickets, HttpStatus.OK);
    }
}

