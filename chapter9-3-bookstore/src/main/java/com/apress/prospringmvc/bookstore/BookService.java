package com.apress.prospringmvc.bookstore;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Iuliana Cosmina on 19/07/2020
 */
public interface BookService {
	Mono<Book> findByIsbn(String isbn);

	Flux<Book> findAll();

	Mono<Book> save(Book book);

	Mono<Void> update(String id, Mono<Book> bookMono);

	Mono<Void> delete(String id);
}
