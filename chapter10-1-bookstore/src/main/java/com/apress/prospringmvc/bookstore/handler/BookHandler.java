/*
Freeware License, some rights reserved

Copyright (c) 2020 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospringmvc.bookstore.handler;

import com.apress.prospringmvc.bookstore.document.Book;
import com.apress.prospringmvc.bookstore.service.BookstoreService;
import com.apress.prospringmvc.bookstore.util.BookSearchCriteria;
import com.apress.prospringmvc.bookstore.util.validation.BookValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import reactor.core.publisher.Mono;

import javax.validation.ValidationException;
import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.*;


/**
 * Created by Iuliana Cosmina on 03/07/2020
 */
@Component
public class BookHandler {
	private final Logger logger = LoggerFactory.getLogger(BookHandler.class);

	private BookstoreService bookstoreService;
	private final Validator validator = new BookValidator();

	public final HandlerFunction<ServerResponse> list;
	public final HandlerFunction<ServerResponse> random;
	public final HandlerFunction<ServerResponse> delete;

	public BookHandler(BookstoreService bookstoreService) {
		this.bookstoreService = bookstoreService;
		list = serverRequest -> ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(bookstoreService.findBooks(new BookSearchCriteria()), Book.class);

		random = serverRequest ->ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(bookstoreService.findRandomBooks(), Book.class);

		delete = serverRequest -> ServerResponse.noContent()
				.build(bookstoreService.deleteBook(serverRequest.pathVariable("isbn")));
	}

	public Mono<ServerResponse> search(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(BookSearchCriteria.class)
				.flatMap(criteria -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
						.body(bookstoreService.findBooks(criteria), Book.class));
	}

	public HandlerFunction<ServerResponse> update = serverRequest -> ServerResponse.noContent()
			.build(bookstoreService.updateByIsbn(serverRequest.pathVariable("isbn"), serverRequest.bodyToMono(Book.class)));

	public Mono<ServerResponse> create(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(Book.class)
				.flatMap(this::validate)
					.flatMap(book -> bookstoreService.addBook(book))
					.flatMap(book -> ServerResponse.created(URI.create("/book/isbn/" + book.getIsbn()))
						.contentType(MediaType.APPLICATION_JSON).bodyValue(book))
				.onErrorResume(error -> ServerResponse.badRequest().bodyValue(error));
	}

	private Mono<Book> validate(Book book) {
		Validator validator = new BookValidator();
		Errors errors = new BeanPropertyBindingResult(book, "book");
		validator.validate(book, errors);
		if (errors.hasErrors()) {
			throw new ValidationException(errors.getAllErrors().toString());
		}
		return Mono.just(book);
	}


	public Mono<ServerResponse> findOne(ServerRequest request) {
		return bookstoreService.findBookByIsbn(request.pathVariable("isbn"))
				.flatMap(book -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(book))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
}
