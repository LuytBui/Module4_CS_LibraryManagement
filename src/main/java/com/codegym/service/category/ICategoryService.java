package com.codegym.service.category;

import com.codegym.model.book.Book;
import com.codegym.model.Category;
import com.codegym.service.IGeneralService;

public interface ICategoryService extends IGeneralService<Category> {
	Iterable<Book> findAllByCategory_Id(Long id);
}
