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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;

/**
 * Created by Iuliana Cosmina on 12/07/2020
 */
@RestController
public class BookController {

	private final BookRepository bookRepository;

	public BookController(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/index")
	public Mono<String> index() {
		return Mono.just("It works");
	}

	// deploy on Tomcat with context chapter9
	//Test it using:  curl -H "Accept:text/event-stream" http://localhost:8080/chapter9/book
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path="/books", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Book> books() {
		Flux<Book> books = bookRepository.findAll();
		Flux<Long> periodFlux = Flux.interval(Duration.ofSeconds(2)); // slowing the stream down
		return books.zipWith(periodFlux).map(Tuple2::getT1);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value="/books")
	public Mono<Book> save(@RequestBody Book book){
		 return bookRepository.save(book);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path="books/{isbn}")
	public Mono<Book> show(@PathVariable String isbn) {
		return bookRepository.findByIsbn(isbn);
	}


}
