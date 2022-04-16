package com.codegym.controller.ticket;

import com.codegym.model.auth.ErrorMessage;
import com.codegym.model.book.Book;
import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.user.User;
import com.codegym.service.book.IBookService;
import com.codegym.service.ticket.IBorrowTicketDetailService;
import com.codegym.service.ticket.IBorrowTicketService;
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
@RequestMapping("/api/borrowtickets")
public class BorrowTicketController {
    ErrorMessage doesntExist = new ErrorMessage("Thẻ mượn không tồn tại");

    @Autowired
    private IBorrowTicketService borrowTicketService;

    @Autowired
    private IBorrowTicketDetailService borrowTicketDetailService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IBookService bookService;

    @GetMapping
    public ResponseEntity<Iterable<BorrowTicket>> findAllBorrowTickets() {
        return new ResponseEntity<>(borrowTicketService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Iterable<BorrowTicket>> findAllBorrowTicketsByCustomerId(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(borrowTicketService.findBorrowTicketByCustomer(userOptional.get()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findBorrowedTicketById(@PathVariable Long id) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(id);
        if (!borrowTicketOptional.isPresent()) {
            return new ResponseEntity<>(doesntExist, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(borrowTicketOptional.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BorrowTicket> createBorrowTicket(@RequestBody BorrowTicket borrowTicket) {
        return new ResponseEntity<>(borrowTicketService.save(borrowTicket), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBorrowTicket(@PathVariable Long id, @RequestBody BorrowTicket newBorrowTicket) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(id);
        if (!borrowTicketOptional.isPresent()) {
            return new ResponseEntity<>(doesntExist, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(borrowTicketService.save(newBorrowTicket), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBorrowTicket(@PathVariable Long id) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(id);
        if (!borrowTicketOptional.isPresent()) {
            return new ResponseEntity<>(doesntExist, HttpStatus.NOT_FOUND);
        }
        borrowTicketService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{id}/accept")
    public ResponseEntity<?> acceptBorrowTicket(@PathVariable Long id) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(id);
        if (!borrowTicketOptional.isPresent()) {
            return new ResponseEntity<>(doesntExist, HttpStatus.NOT_FOUND);
        }

        BorrowTicket borrowTicket = borrowTicketOptional.get();
        List<Book> books = borrowTicketDetailService.findAllBookByBorrowTicket(borrowTicket);

        for (Book book : books) {
            if (book.getQuantity() <= 0) {
                String message = "Sách " + book.getName() + " đã hết rồi";
                ErrorMessage errorMessage = new ErrorMessage(message);
                return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
            }
        }

        for (Book book: books) {
            book.setQuantity(book.getQuantity() - 1);
            bookService.save(book);
        }

        borrowTicket.setReviewed(true);
        borrowTicket.setAccepted(true);
        borrowTicket.setReturned(false);
        borrowTicket.setBorrowDate(getCurrentTime());
        return new ResponseEntity<>(borrowTicketService.save(borrowTicket), HttpStatus.OK);
    }

    @PostMapping("/{id}/deny")
    public ResponseEntity<?> denyBorrowTicket(@PathVariable Long id) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(id);
        if (!borrowTicketOptional.isPresent()) {
            return new ResponseEntity<>(doesntExist, HttpStatus.NOT_FOUND);
        }
        BorrowTicket borrowTicket = borrowTicketOptional.get();
        borrowTicket.setReviewed(true);
        borrowTicket.setAccepted(false);
        return new ResponseEntity<>(borrowTicketService.save(borrowTicket), HttpStatus.OK);
    }

    public String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return String.valueOf(now);
    }

    @GetMapping("/{id}/books")
    public ResponseEntity<?> findBooksInTicket(@PathVariable Long id) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(id);
        if (!borrowTicketOptional.isPresent()) {
            return new ResponseEntity<>(doesntExist, HttpStatus.NOT_FOUND);
        }
        List<Book> books = borrowTicketDetailService.findAllBookByBorrowTicket(borrowTicketOptional.get());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<?> showBorrowTicketDetails(@PathVariable Long id) {
        Optional<BorrowTicket> borrowTicketOptional = borrowTicketService.findById(id);
        if (!borrowTicketOptional.isPresent()) {
            return new ResponseEntity<>(doesntExist, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(borrowTicketOptional, HttpStatus.OK);
    }
}
