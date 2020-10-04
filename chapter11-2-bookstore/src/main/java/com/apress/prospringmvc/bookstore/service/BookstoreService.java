package com.apress.prospringmvc.bookstore.service;

import com.apress.prospringmvc.bookstore.document.*;
import com.apress.prospringmvc.bookstore.util.BookSearchCriteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Created by Iuliana Cosmina on 27/07/2020
 */
public interface BookstoreService {

	Flux<Book> findBooksByCategory(String category);

	Mono<Book> findBook(String id);

	Mono<Book> findBookByIsbn(String isbn);

	Flux<Book> findRandomBooks();

	Mono<Order> findOrder(String id);

	Mono<List<Order>> findOrdersForAccountId(String accountId);

	Flux<Book> findBooks(BookSearchCriteria bookSearchCriteria);

	Mono<Order> createOrder(Cart cart, Account account);

	Mono<Book> addBook(Book book);

	Mono<Void> deleteBook(String bookIsbn);

	Flux<Book> randomBookNews();

}
