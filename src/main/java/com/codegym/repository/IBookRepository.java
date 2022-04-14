package com.codegym.repository;

import com.codegym.model.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBookRepository extends PagingAndSortingRepository<Book, Long> {
    Iterable<Book> findAllByCategory_Id(Long id);
    Page<Book> findAllByNameContaining(String name, Pageable pageable);
    Page<Book> findAllByCategory_Id(Long id, Pageable pageable);
    Page<Book> findAllByPublisher(String publisher, Pageable pageable);

    @Query(value = "select publisher from books group by publisher", nativeQuery = true)
    List<String> findAllPublisher ();
}
