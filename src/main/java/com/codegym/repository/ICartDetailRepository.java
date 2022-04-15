package com.codegym.repository;

import com.codegym.model.book.Book;
import com.codegym.model.cart.Cart;
import com.codegym.model.cart.CartDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICartDetailRepository extends PagingAndSortingRepository<CartDetail, Long> {
    Page<CartDetail> findAll(Pageable pageable);

    @Query(value = "SELECT cdt.book FROM CartDetail cdt INNER JOIN cdt.book WHERE cdt.cart = ?1")
    List<Book> findAllBookInCart(Cart cart);


}
