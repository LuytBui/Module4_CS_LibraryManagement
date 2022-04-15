package com.codegym.service.cart;

import com.codegym.model.book.Book;
import com.codegym.model.cart.CartDetail;
import com.codegym.repository.ICartDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartDetailService implements ICartDetailService {
    @Autowired
    private ICartDetailRepository cartDetailRepository;

    @Override
    public Iterable<CartDetail> findAll() {
        return cartDetailRepository.findAll();
    }

    @Override
    public CartDetail save(CartDetail cartDetail) {
        return cartDetailRepository.save(cartDetail);
    }

    @Override
    public void deleteById(Long id) {
        cartDetailRepository.deleteById(id);
    }

    @Override
    public Optional<CartDetail> findById(Long id) {
        return cartDetailRepository.findById(id);
    }

    @Override
    public Page<CartDetail> findAll(Pageable pageable) {
        return cartDetailRepository.findAll(pageable);
    }

    @Override
    public List<Book> findAllBookCartDetail(Pageable pageable) {
        return cartDetailRepository.findAllBookCartDetail(pageable);
    }
}
