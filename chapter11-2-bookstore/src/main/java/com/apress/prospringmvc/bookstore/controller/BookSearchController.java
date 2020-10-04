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
import com.apress.prospringmvc.bookstore.util.BookSearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.spring5.context.webflux.IReactiveSSEDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.apress.prospringmvc.bookstore.document.Book.Category.*;
import static com.apress.prospringmvc.bookstore.util.ServiceProblems.*;

/**
 * Created by Iuliana Cosmina on 27/07/2020
 */
@Controller
public class BookSearchController {
	private final Logger logger = LoggerFactory.getLogger(BookSearchController.class);

	private BookstoreService bookstoreService;

	public BookSearchController(BookstoreService bookstoreService) {
		this.bookstoreService = bookstoreService;
	}

	@ModelAttribute("categories")
	public List<String> getCategories() {
		return List.of(WEB, SPRING, JAVA);
	}

	@ModelAttribute
	public BookSearchCriteria criteria() {
		return new BookSearchCriteria();
	}

	@GetMapping(path = "/book/search")
	public String load(){
		return "book/search";
	}

	@RequestMapping( value = "/book/new", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public String newBooks(final Model model){
		WebClient webClient = WebClient.builder()
				.baseUrl("http://localhost:8080/book/random-news")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_EVENT_STREAM_VALUE)
				.defaultCookie("InternalCookie", "all")
				.build();

		Flux<Book> newReleases = webClient.get()
				.uri("/")
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, response ->
						Mono.error( response.statusCode() == HttpStatus.UNAUTHORIZED
							? new ServiceDeniedException("You shall not pass!")
 							: new ServiceDeniedException("Well.. this is unfortunate!"))
				)
				.onStatus(HttpStatus::is5xxServerError, response ->
						Mono.error(response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR
							? new ServiceDownException("This is SpartAAA!!")
							: new ServiceDownException("Well.. this is a mistery!"))
				)
				.bodyToFlux(Book.class);

		final IReactiveSSEDataDriverContextVariable dataDriver =
				new ReactiveDataDriverContextVariable(newReleases, 1, "newBooks");

		model.addAttribute("newBooks", dataDriver);   // Flux wrapped in a DataDriver to avoid resolution
		// Will use the same "book/search" template, but only a fragment: the newBooks block.
		return "book/search :: #newBooks";
	}

	@RequestMapping( value = "/tech/news", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public String techNews(final Model model){
		final WebClient webClient = WebClient.create("http://localhost:3000/techNews");

		Flux<String> newReleases = webClient.get().uri("/")
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, response ->
						Mono.error( response.statusCode() == HttpStatus.UNAUTHORIZED
								? new ServiceDeniedException("You shall not pass!")
								: new ServiceDeniedException("Well.. this is unfortunate!"))
				)
				.onStatus(HttpStatus::is5xxServerError, response ->
						Mono.error(response.statusCode() == HttpStatus.INTERNAL_SERVER_ERROR
								? new ServiceDownException("This is SpartAAA!!")
								: new ServiceDownException("Well.. this is a mystery!"))
				)
				.bodyToFlux(String.class);

		final IReactiveSSEDataDriverContextVariable dataDriver =
				new ReactiveDataDriverContextVariable(newReleases, 1, "techNews");

		model.addAttribute("techNews", dataDriver);
		return "book/search :: #techNews";
	}
}

