package com.codegym.service.cart;

import com.codegym.model.cart.Cart;
import com.codegym.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICartService extends IGeneralService<Cart> {
    Page<Cart> findAll(Pageable pageable);

    Cart findCartByUser_Id(Long userId);
}
