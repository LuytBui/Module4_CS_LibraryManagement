package com.codegym.controller.cart;

import com.codegym.model.book.Book;
import com.codegym.model.cart.Cart;
import com.codegym.model.cart.CartDetail;
import com.codegym.service.book.IBookService;
import com.codegym.service.cart.ICartDetailService;
import com.codegym.service.cart.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin("*")
@RequestMapping("/api/carts")
public class CartController {
    @Autowired
    private ICartService cartService;

    @Autowired
    private ICartDetailService cartDetailService;

    @Autowired
    IBookService bookService;

    @GetMapping
    public ResponseEntity<Page<Cart>> findAllCart(Pageable pageable) {
        return new ResponseEntity<>(cartService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findAllBookInCart(@PathVariable Long id) {
        Optional<Cart> cartOptional = cartService.findById(id);
        if (!cartOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Book> books = cartDetailService.findAllBookInCart(cartOptional.get());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/{cartId}/add-book/{bookId}")
    public ResponseEntity<Cart> save(@PathVariable Long cartId, @PathVariable Long bookId) {
        Optional<Cart> cartOptional = cartService.findById(cartId);
        Optional<Book> bookOptional = bookService.findById(bookId);
        if (!cartOptional.isPresent() || !bookOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // lay ra user dang dang nhap tu DB
        // lay ra cart ung voi user nay
        // so sanh 2 cart id
        // neu khac nhau thi return bad request

        cartDetailService.addBookToCart(cartOptional.get(), bookOptional.get());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{cartId}/remove-book/{bookId}")
    public ResponseEntity<?> removeBook(@PathVariable Long cartId, @PathVariable Long bookId) {
        Optional<Cart> cartOptional = cartService.findById(cartId);
        Optional<Book> bookOptional = bookService.findById(bookId);
        if (!cartOptional.isPresent() || !bookOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        cartDetailService.removeBookFromCart(cartOptional.get(),bookOptional.get());
        return findAllBookInCart(cartId);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Cart> deleteCart(@PathVariable Long id) {
        Optional<Cart> cartOptional = cartService.findById(id);
        if (!cartOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        cartService.deleteById(id);
        return new ResponseEntity<>(cartOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/get-cart-of-user/{userId}")
    public ResponseEntity<?> getCartOfUser(@PathVariable Long userId){
        Cart cart = cartService.findCartByUser_Id(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

}

