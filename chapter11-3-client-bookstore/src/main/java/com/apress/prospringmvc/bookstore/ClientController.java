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

import com.apress.prospringmvc.bookstore.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Iuliana Cosmina on 17/08/2020
 */
@RestController
public class ClientController {

	private final RSocketRequester requester;

	@Autowired
	public ClientController(RSocketRequester requester) {
		this.requester = requester;
	}

	@GetMapping("/")
	public Mono<String> index(){
		return Mono.just("It works!");
	}

	//open in browser
	@GetMapping("introduction")
	public Mono<String> introduction(){
		ClientMessage clientMessage = new ClientMessage().name("gigi");
		requester.route("introduction").data(clientMessage).send();
		return Mono.just("Introduction data was sent.");
	}

	// open in browser
	@GetMapping("check-service")
	public Mono<String> checkService(){
		ClientMessage clientMessage = new ClientMessage().name("gigi");
		return requester.route("check-service").data(clientMessage).retrieveMono(String.class);
	}

	//curl -H "Accept:text/event-stream" http://localhost:8080/show-books
	@GetMapping(path = "show-books", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Book> showBooks(){
		ClientMessage clientMessage = new ClientMessage().name("gigi");
		return requester.route("show-books").data(clientMessage).retrieveFlux(Book.class).limitRate(20);
	}

	//curl -H "Accept:text/event-stream" http://localhost:8080/books-channel
	@GetMapping(value = "books-channel", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	Flux<Book> booksChannel() {
		return this.requester.route("books-channel")
				.data(Flux.range(0, 10).map(i -> new ClientMessage().name("gigi").author(RandomUtil.randomAuthor())))
				.retrieveFlux(Book.class).limitRate(5).log();
	}

}
