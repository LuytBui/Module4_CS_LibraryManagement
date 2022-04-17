package com.codegym.service.cart;

import com.codegym.model.cart.Cart;
import com.codegym.model.user.User;
import com.codegym.repository.ICartRepository;
import com.codegym.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CartService implements ICartService {
    @Autowired
    private ICartRepository cartRepository;

    @Autowired
    private IUserService userService;

    @Override
    public Iterable<Cart> findAll() {
        return cartRepository.findAll();
    }

    @Override
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public void deleteById(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public Optional<Cart> findById(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public Page<Cart> findAll(Pageable pageable) {
        return cartRepository.findAll(pageable);
    }

    @Override
    public Cart findCartByUser_Id(Long userId) {
        Optional<User> findUser = userService.findById(userId);
        if (!findUser.isPresent())
            return null;

        Optional<Cart> findCart = cartRepository.findByCustomer_Id(userId);

        if (!findCart.isPresent()){
            Cart cart = new Cart (findUser.get());
            save(cart);
            return cart;
        }

        return findCart.get();
        /*
        neu trong DB da co Cart ung voi user nay thi tra ve
        neu chua co thi tao moi, luu vao DB, tra ve
         */
    }

}
