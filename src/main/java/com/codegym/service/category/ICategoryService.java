package com.codegym.service.category;

import com.codegym.model.Book;
import com.codegym.model.Category;
import com.codegym.service.IGeneralService;

public interface ICategoryService extends IGeneralService<Category> {
	Iterable<Book> findAllByCategory_Id(Long id);
}
