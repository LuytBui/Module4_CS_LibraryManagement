package com.codegym.repository;

import com.codegym.model.cart.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICartRepository extends PagingAndSortingRepository<Cart, Long> {
    Page<Cart> findAll(Pageable pageable);

}
