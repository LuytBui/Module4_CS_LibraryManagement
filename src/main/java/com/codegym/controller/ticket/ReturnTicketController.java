package com.codegym.controller.ticket;

import com.codegym.model.auth.ErrorMessage;
import com.codegym.model.book.Book;
import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.ticket.ReturnTicket;
import com.codegym.model.user.User;
import com.codegym.service.book.IBookService;
import com.codegym.service.ticket.IBorrowTicketDetailService;
import com.codegym.service.ticket.IBorrowTicketService;
import com.codegym.service.ticket.IReturnTicketService;
import com.codegym.service.user.IUserService;
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

    @Autowired
    private IBookService bookService;

    @Autowired
    private IBorrowTicketDetailService borrowTicketDetailService;

    @Autowired
    private IUserService userService;

    @GetMapping
    public ResponseEntity<Iterable<ReturnTicket>> findAll() {
        Iterable<ReturnTicket> returnTickets = returnTicketService.findAll();
        return new ResponseEntity<>(returnTickets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id){
        Optional<ReturnTicket> returnTicketOptional = returnTicketService.findById(id);
        if (!returnTicketOptional.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(returnTicketOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/customer/{userId}")
    public ResponseEntity<?> findReturnTicketByUser(@PathVariable Long userId) {
        Optional<User>  userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<ReturnTicket> returnTickets = returnTicketService.findAllByCustomer_Id(userId);
        return new ResponseEntity<>(returnTickets, HttpStatus.OK);
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
        returnTicket.setBorrowTicket(borrowTicket);
        returnTicketService.save(returnTicket);
        return new ResponseEntity<>(returnTicket, HttpStatus.CREATED);
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

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptReturnTicket(@PathVariable Long id) {
        Optional<ReturnTicket> returnTicketOptional = returnTicketService.findById(id);
        if (!returnTicketOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ReturnTicket returnTicket = returnTicketOptional.get();
        BorrowTicket borrowTicket = returnTicket.getBorrowTicket();
        List<Book> books = borrowTicketDetailService.findAllBookByBorrowTicket(borrowTicket);
        // lấy ra borrowTicket
        // lay ra danh sach books cua borrowTicket
        // dùng for,+1 quantity cho từng book trong books

        // set isReturned cho thằng borrow Ticket
        // lưu borrowTIcket vào DB
        for (Book book : books) {
            book.setQuantity(book.getQuantity() + 1);
            bookService.save(book);
        }

        borrowTicket.setReturned(true);
        borrowTicketService.save(borrowTicket);

        returnTicket.setReviewed(true);
        returnTicket.setAccepted(true);
        returnTicket.setReturnDate(getCurrentTime());
        returnTicketService.save(returnTicket);
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

        BorrowTicket borrowTicket = returnTicket.getBorrowTicket();
        borrowTicket.setHasReturnTicket(false);
        borrowTicketService.save(borrowTicket);

        return new ResponseEntity<>(returnTicketService.save(returnTicket), HttpStatus.OK);
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

