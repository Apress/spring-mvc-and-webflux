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
package com.apress.prospringmvc.bookstore.controller;

import com.apress.prospringmvc.bookstore.document.Book;
import com.apress.prospringmvc.bookstore.service.BookstoreService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Iuliana Cosmina on 29/07/2020
 */
@Controller
public class BookDetailController {

	final BookstoreService bookstoreService;

	public BookDetailController(BookstoreService bookstoreService) {
		this.bookstoreService = bookstoreService;
	}

	@ResponseBody
	@GetMapping(value = "/book/id/{id}")
	public Mono<Book> getBookById(@PathVariable String id) {
		return bookstoreService.findBook(id);
	}

	@ResponseBody
	@GetMapping(value = "/book/isbn/{isbn}")
	public Mono<Book> getBookByIsbn(@PathVariable String isbn) {
		return bookstoreService.findBookByIsbn(isbn);
	}

	@GetMapping(value = "/book/detail/{bookId}")
	public String details(@PathVariable String bookId, Model model) {
		WebClient webClient = WebClient.create("http://localhost:8080/book");

		Flux<Book> bookFlux = webClient.get()
				.uri(
						uriBuilder -> uriBuilder.path("/id/{id}")
								.build(bookId)
				 )
				.retrieve()
				.bodyToMono(Book.class)
				.flux();

		IReactiveDataDriverContextVariable dataDriver =
				new ReactiveDataDriverContextVariable( bookFlux,1);

		model.addAttribute("books", dataDriver);

		return "book/detail";
	}
}
