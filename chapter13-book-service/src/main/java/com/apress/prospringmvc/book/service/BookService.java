package com.apress.prospringmvc.book.service;

import com.apress.prospringmvc.book.Book;
import com.apress.prospringmvc.book.BookSearchCriteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Iuliana Cosmina on 27/07/2020
 */
public interface BookService {

	Flux<Book> findBooksByCategory(String category);

	Mono<Book> findBook(String id);

	Flux<Book> findRandomBooks();

	Mono<Book> findBookByIsbn(String isbn);

	Flux<Book> findBooks(BookSearchCriteria bookSearchCriteria);

	Mono<Book> addBook(Book book);

	Mono<Void> deleteBook(String bookIsbn);

	Mono<Void> updateByIsbn(String isbn, Mono<Book> bookMono);

}
