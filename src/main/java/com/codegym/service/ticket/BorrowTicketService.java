package com.codegym.service.ticket;

import com.codegym.exception.EmptyCartException;
import com.codegym.model.book.Book;
import com.codegym.model.cart.Cart;
import com.codegym.model.ticket.BorrowTicket;
import com.codegym.model.ticket.BorrowTicketDetail;
import com.codegym.model.user.User;
import com.codegym.repository.IBorrowTicketRepository;
import com.codegym.service.cart.ICartDetailService;
import com.codegym.service.cart.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class BorrowTicketService implements IBorrowTicketService {
    @Autowired
    private IBorrowTicketRepository borrowTicketRepository;

    @Autowired
    private IBorrowTicketDetailService borrowTicketDetailService;

    @Autowired
    private IBorrowTicketService borrowTicketService;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ICartDetailService cartDetailService;

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

    @Override
    public BorrowTicket createBorrowTicket(User user, int duration) throws EmptyCartException {
        Cart cart = cartService.findCartByUser_Id(user.getId());
        List<Book> books = cartDetailService.findAllBookInCart(cart);
        if (books.size() == 0) {
            throw new EmptyCartException();
        }

        BorrowTicket borrowTicket = new BorrowTicket();
        borrowTicket = borrowTicketService.save(borrowTicket);  // important: get an id for borrowTicket
        borrowTicket.setCustomer(user);

        for (Book book : books) {
            BorrowTicketDetail borrowTicketDetail = new BorrowTicketDetail();
            borrowTicketDetail.setBorrowTicket(borrowTicket);
            borrowTicketDetail.setBook(book);
            borrowTicketDetailService.save(borrowTicketDetail);
        }

        borrowTicket.setDuration(duration);
        borrowTicket.setBorrowDate(getCurrentTime());
        borrowTicketRepository.save(borrowTicket);
        return borrowTicket;
    }

    public String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return String.valueOf(now);
    }
}
