package com.apress.prospringmvc.bookstore.repository;

import com.apress.prospringmvc.bookstore.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for working with {@link Category} domain objects
 *  
 * @author Marten Deinum
 */
public interface CategoryRepository extends JpaRepository<Category, Long> { }
