package com.codegym.controller.ticket;

import com.codegym.exception.EmptyCartException;
import com.codegym.model.auth.ErrorMessage;
import com.codegym.model.book.Book;
import com.codegym.model.cart.Cart;
import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.user.User;
import com.codegym.repository.ICartDetailRepository;
import com.codegym.service.book.IBookService;
import com.codegym.service.cart.ICartDetailService;
import com.codegym.service.cart.ICartService;
import com.codegym.service.ticket.IBorrowTicketDetailService;
import com.codegym.service.ticket.IBorrowTicketService;
import com.codegym.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/borrowTickets")
public class BorrowTicketController {
    public static int PAGE_SIZE = 5;

    ErrorMessage doesntExist = new ErrorMessage("Thẻ mượn không tồn tại");

    @Autowired
    private IBorrowTicketService borrowTicketService;

    @Autowired
    private IBorrowTicketDetailService borrowTicketDetailService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IBookService bookService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ICartDetailService cartDetailService;

    @GetMapping
    public ResponseEntity<Page<BorrowTicket>> findBorrowTicketNotReviewed(@RequestParam(name = "page") Long page) {
        if (page == null) page = 0L;
        Pageable pageable = PageRequest.of(page.intValue(), PAGE_SIZE);
        Page<BorrowTicket> borrowTicketPage = borrowTicketService.findBorrowTicketNotReviewed(pageable);
        return new ResponseEntity<>(borrowTicketPage, HttpStatus.OK);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<Iterable<BorrowTicket>> findAllBorrowTicketsByCustomerId(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(borrowTicketService.findBorrowTicketByCustomer(userOptional.get()), HttpStatus.OK);
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

        boolean outOfStock = false;
        String message = "";
        for (Book book : books) {
            if (book.getQuantity() <= 0) {
                outOfStock = true;
                message += "Sách " + book.getName() + " đã hết, ";
            }
        }
        if (outOfStock) {
            ErrorMessage errorMessage = new ErrorMessage(message);
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }


        for (Book book : books) {
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

    @PostMapping("/{userId}")
    public ResponseEntity<?> createBorrowTicket(@PathVariable Long userId, @RequestParam int duration) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            User user = userOptional.get();
            Cart cart = cartService.findCartByUser_Id(userId);


            BorrowTicket borrowTicket = borrowTicketService.createBorrowTicket(user, duration);
            cartDetailService.removeAllBookFromCart(cart);// remove all books from cart
            return new ResponseEntity<>(borrowTicket, HttpStatus.CREATED);
        } catch (EmptyCartException e) {
            ErrorMessage errorMessage = new ErrorMessage("Giỏ sách đang trống");
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getBorrowTicketsOfUser(@PathVariable Long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Iterable<BorrowTicket> borrowTickets = borrowTicketService.findBorrowTicketByCustomer(userOptional.get());
        return new ResponseEntity<>(borrowTickets, HttpStatus.OK);
    }


}
