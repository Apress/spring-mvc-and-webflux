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
package com.apress.prospringmvc.book.handler;

import com.apress.prospringmvc.book.Book;
import com.apress.prospringmvc.book.BookSearchCriteria;
import com.apress.prospringmvc.book.service.BookService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Created by Iuliana Cosmina on 29/08/2020
 */
@Service
public class BookHandler {

	private final BookService bookService;
	public HandlerFunction<ServerResponse> update;
	public HandlerFunction<ServerResponse> delete;
	public HandlerFunction<ServerResponse> random;

	public BookHandler(BookService bookService) {
		this.bookService = bookService;

		update = serverRequest -> ServerResponse.noContent()
				.build(bookService.updateByIsbn(serverRequest.pathVariable("isbn"), serverRequest.bodyToMono(Book.class)));
		delete = serverRequest -> ServerResponse.noContent()
				.build(bookService.deleteBook(serverRequest.pathVariable("isbn")));
		random = serverRequest -> ok()
				.contentType(MediaType.TEXT_EVENT_STREAM)
				.body(bookService.findRandomBooks(), Book.class);
	}

	public final HandlerFunction<ServerResponse> main = serverRequest -> ok()
			.contentType(MediaType.TEXT_HTML)
			.bodyValue("Book service up and running!");

	public Mono<ServerResponse> search(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(BookSearchCriteria.class)
				.flatMap(criteria -> ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
						.body(bookService.findBooks(criteria), Book.class));
	}

	public Mono<ServerResponse> findOne(ServerRequest request) {
		return bookService.findBookByIsbn(request.pathVariable("isbn"))
				.flatMap(book -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(book))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	public Mono<ServerResponse> create(ServerRequest serverRequest) {
		return serverRequest.bodyToMono(Book.class)
				.flatMap(book -> bookService.addBook(book))
				.flatMap(book -> ServerResponse.created(URI.create("/book/isbn/" + book.getIsbn()))
						.contentType(MediaType.APPLICATION_JSON).bodyValue(book));
	}
}

