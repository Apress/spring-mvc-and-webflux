package com.apress.prospringmvc.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.apress.prospringmvc.bookstore.domain.Category;
import com.apress.prospringmvc.bookstore.repository.CategoryRepository;

/**
 * @see CategoryService
 * @author Marten Deinum
 *
 */
@Service
@Transactional(readOnly = true)
class CategoryServiceImpl implements CategoryService {

	private final CategoryRepository categoryRepository;

	CategoryServiceImpl(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Category findById(long id) {
		return categoryRepository.findById(id).orElse(null);
	}

	@Override
	public Iterable<Category> findAll() {
		return this.categoryRepository.findAll();
	}

	@Override
	public void addCategory(Category category) {
		categoryRepository.save(category);
	}
}
