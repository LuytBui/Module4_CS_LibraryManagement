package com.codegym.service.cart;

import com.codegym.model.book.Book;
import com.codegym.model.cart.Cart;
import com.codegym.model.cart.CartDetail;
import com.codegym.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICartDetailService extends IGeneralService<CartDetail> {
    Page<CartDetail> findAll(Pageable pageable);

    List<Book> findAllBookInCart(Cart cart);

    void addBookToCart(Cart cart, Book book);

    void removeBookFromCart(Cart cart, Book book);

    void removeAllBookFromCart(Cart cart);
}
