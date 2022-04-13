package com.codegym.service;

import com.codegym.model.ticket.BorrowTicket;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IGeneralService<T> {
    Iterable<T> findAll();

    T save(T t);

    void deleteById(Long id);

    Optional<T> findById(Long id);

}
