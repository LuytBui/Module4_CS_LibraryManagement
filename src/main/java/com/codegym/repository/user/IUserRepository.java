package com.codegym.repository.user;

import com.codegym.model.user.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
