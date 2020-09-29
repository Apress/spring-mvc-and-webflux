package com.apress.prospringmvc.bookstore.service;

import com.apress.prospringmvc.bookstore.domain.Category;

/**
 * Contract for services that work with an {@link Category}.
 * 
 * @author Marten Deinum

 * 
 */
public interface CategoryService {

	Category findById(long id);

	Iterable<Category> findAll();

	void addCategory(Category category);

}
