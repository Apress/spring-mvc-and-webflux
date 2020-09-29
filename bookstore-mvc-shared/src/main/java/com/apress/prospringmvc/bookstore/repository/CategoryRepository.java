package com.apress.prospringmvc.bookstore.repository;

import com.apress.prospringmvc.bookstore.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Repository for working with {@link Category} domain objects
 *  
 * @author Marten Deinum
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

	@Query("select c from Category c where c.name=:name")
	Optional<Category> findByName(@Param("name")String name);
}
