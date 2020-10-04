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
package com.apress.prospringmvc.bookstore;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * Created by Iuliana Cosmina on 19/07/2020
 */
@Component
public class BookHandler {

	private final BookService bookService;
	public HandlerFunction<ServerResponse> list;
	public HandlerFunction<ServerResponse> delete;

	public BookHandler(BookService bookService) {
		this.bookService = bookService;

		/* 1 */
		list = serverRequest -> ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON).body(bookService.findAll(), Book.class);

		/* 2 */
		delete = serverRequest -> ServerResponse.noContent()
				.build(bookService.delete(serverRequest.pathVariable("id")));
	}

	/* 3 */
	public Mono<ServerResponse> findByIsbn(ServerRequest serverRequest) {
		Mono<Book> bookMono = bookService.findByIsbn(serverRequest.pathVariable("isbn"));
		return bookMono
				.flatMap(book -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(book))
				.switchIfEmpty(ServerResponse.notFound().build());
	}

	/* 4 */
	public Mono<ServerResponse> save(ServerRequest serverRequest) {
		Mono<Book> bookMono =  serverRequest.bodyToMono(Book.class).doOnNext(bookService::save);
		return bookMono
				.flatMap(book -> ServerResponse.created(URI.create("/books/" + book.getId()))
						.contentType(MediaType.APPLICATION_JSON).bodyValue(book))
				.switchIfEmpty(ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
	}
}
