package com.codegym.repository.user;

import com.codegym.model.user.Role;
import com.codegym.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Page<User> findAllByRole_Name(String role_name, Pageable pageable);
}
