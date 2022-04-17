package com.codegym.service.user;

import com.codegym.model.auth.UserPrincipal;
import com.codegym.model.user.Role;
import com.codegym.model.user.User;
import com.codegym.repository.user.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    IUserRepository userRepository;

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) throws EntityNotFoundException {
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<User> finddAllUserByRole_Name(String roleRame, Pageable pageable) {
        return userRepository.findAllByRole_Name(roleRame, pageable);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent() || !user.get().isActive())
            throw new UsernameNotFoundException("Tài khoản không tồn tại hoặc đã bị khóa");
        return UserPrincipal.build(user.get());
    }
}
