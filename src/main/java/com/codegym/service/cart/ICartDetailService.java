package com.codegym.service.cart;

import com.codegym.model.book.Book;
import com.codegym.model.cart.CartDetail;
import com.codegym.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICartDetailService extends IGeneralService<CartDetail> {
    Page<CartDetail> findAll(Pageable pageable);
    List<Book> findAllBookCartDetail(Pageable pageable);

}
