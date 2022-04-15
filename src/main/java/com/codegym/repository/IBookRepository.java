package com.codegym.repository;

import com.codegym.model.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBookRepository extends PagingAndSortingRepository<Book, Long> {
    Iterable<Book> findAllByCategory_Id(Long id);
    Page<Book> findAllByNameContaining(String name, Pageable pageable);
}
