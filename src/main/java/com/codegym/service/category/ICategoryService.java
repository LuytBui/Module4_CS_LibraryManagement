package com.codegym.service.category;

import com.codegym.model.book.Book;
import com.codegym.model.Category;
import com.codegym.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService extends IGeneralService<Category> {
	Iterable<Book> findAllByCategory_Id(Long id);
	Page<Book> findAllByCategory_Id(Long id, Pageable pageable);
}
