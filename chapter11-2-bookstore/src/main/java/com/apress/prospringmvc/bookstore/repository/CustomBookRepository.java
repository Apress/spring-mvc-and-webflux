package com.apress.prospringmvc.bookstore.repository;

import com.apress.prospringmvc.bookstore.document.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

/**
 * Created by Iuliana Cosmina on 26/07/2020
 */
public interface CustomBookRepository {
	Flux<Book> findRandom(Pageable pageable);

	Flux<Book> findAll(Query query);
}
