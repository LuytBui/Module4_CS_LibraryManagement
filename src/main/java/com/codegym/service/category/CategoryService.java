package com.codegym.service.category;

import com.codegym.model.book.Book;
import com.codegym.model.Category;
import com.codegym.repository.IBookRepository;
import com.codegym.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService implements ICategoryService{
	@Autowired
	IBookRepository bookRepository;
	@Autowired
	private ICategoryRepository categoryRepository;

	@Override
	public Iterable<Category> findAll() {
		return categoryRepository.findAll();
	}

	@Override
	public Optional<Category> findById(Long id) {
		return categoryRepository.findById(id);
	}

	@Override
	public Category save(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public void deleteById(Long id) {
		categoryRepository.deleteById(id);
	}

	@Override
	public Iterable<Book> findAllByCategory_Id(Long id) {
		return bookRepository.findAllByCategory_Id(id);
	}

	@Override
	public Page<Book> findAllByCategory_Id(Long id, Pageable pageable) {
		return bookRepository.findAllByCategory_Id(id, pageable);
	}
}
