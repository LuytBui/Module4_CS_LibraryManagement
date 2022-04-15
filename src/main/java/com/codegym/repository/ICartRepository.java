package com.codegym.repository;

import com.codegym.model.cart.Cart;
import com.codegym.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ICartRepository extends PagingAndSortingRepository<Cart, Long> {
    Page<Cart> findAll(Pageable pageable);
    Optional<Cart> findByCustomer_Id(Long customerId);
}
