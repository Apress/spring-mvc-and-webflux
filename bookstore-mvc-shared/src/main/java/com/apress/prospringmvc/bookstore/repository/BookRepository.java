package com.apress.prospringmvc.bookstore.repository;

import com.apress.prospringmvc.bookstore.domain.Book;
import com.apress.prospringmvc.bookstore.domain.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository for working with {@link Book} domain objects
 * 
 * @author Marten Deinum
 *
 */
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

	List<Book> findByCategory(Category category);

	@Query("SELECT b FROM Book b ORDER BY rand()")
	List<Book> findRandom(Pageable pageagble);
}
