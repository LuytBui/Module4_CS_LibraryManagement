package com.codegym.controller.cart;

import com.codegym.model.auth.ErrorMessage;
import com.codegym.model.book.Book;
import com.codegym.model.cart.Cart;
import com.codegym.model.user.User;
import com.codegym.service.book.IBookService;
import com.codegym.service.cart.ICartDetailService;
import com.codegym.service.cart.ICartService;
import com.codegym.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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

    @Autowired
    IUserService userService;

    @GetMapping
    public ResponseEntity<Page<Cart>> findAllCart(Pageable pageable) {
        return new ResponseEntity<>(cartService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findAllBookInCartOfUser(@PathVariable Long userId) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(new ErrorMessage("Khách hàng không tồn tại"), HttpStatus.BAD_REQUEST);
        }

        Cart cart = cartService.findCartByUser_Id(userId);

        List<Book> books = cartDetailService.findAllBookInCart(cart);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PostMapping("/{userId}/add-book/{bookId}")
    public ResponseEntity<?> addBookToCart(@PathVariable Long userId, @PathVariable Long bookId) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(new ErrorMessage("Khách hàng không tồn tại"), HttpStatus.BAD_REQUEST);
        }
        Cart cart = cartService.findCartByUser_Id(userId);

        Optional<Book> bookOptional = bookService.findById(bookId);
        if (!bookOptional.isPresent()) {
            return new ResponseEntity<>(new ErrorMessage("Mã sách không tồn tại"), HttpStatus.BAD_REQUEST);
        }

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        String username = principal.getName();
        User user = userService.findByUsername(username).get();
        if (!user.getId().equals(userId)){ // kiểm tra id của User đang đăng nhập với id trong PathVariable
            return new ResponseEntity<>(new ErrorMessage("Không có quyền edit cart của người dùng khác"), HttpStatus.BAD_REQUEST);
        }

        cartDetailService.addBookToCart(cart, bookOptional.get());

        List<Book> books = cartDetailService.findAllBookInCart(cart);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/remove-book/{bookId}")
    public ResponseEntity<?> removeBookFromCart(@PathVariable Long userId, @PathVariable Long bookId) {
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(new ErrorMessage("Khách hàng không tồn tại"), HttpStatus.BAD_REQUEST);
        }
        Cart cart = cartService.findCartByUser_Id(userId);

        Optional<Book> bookOptional = bookService.findById(bookId);
        if (!bookOptional.isPresent()) {
            return new ResponseEntity<>(new ErrorMessage("Mã sách không tồn tại"), HttpStatus.BAD_REQUEST);
        }

        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        String username = principal.getName();
        User user = userService.findByUsername(username).get();
        if (!user.getId().equals(userId)){ // kiểm tra id của User đang đăng nhập với id trong PathVariable
            return new ResponseEntity<>(new ErrorMessage("Không có quyền edit cart của người dùng khác"), HttpStatus.BAD_REQUEST);
        }

        cartDetailService.removeBookFromCart(cart, bookOptional.get());

        List<Book> books = cartDetailService.findAllBookInCart(cart);
        return new ResponseEntity<>(books, HttpStatus.OK);
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
        Optional<User> userOptional = userService.findById(userId);
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(new ErrorMessage("Khách hàng không tồn tại"), HttpStatus.BAD_REQUEST);
        }
        Cart cart = cartService.findCartByUser_Id(userId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}

