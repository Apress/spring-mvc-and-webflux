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

import com.apress.prospringmvc.bookstore.ClientMessage;
import com.apress.prospringmvc.bookstore.book.Book;
import com.apress.prospringmvc.bookstore.util.BookNewReleasesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

/**
 * @author Iuliana Cosmina
 * @date 09/08/2020
 */
@Controller
public class ServerController {
	private final Logger logger = LoggerFactory.getLogger(ServerController.class);


	// test using: `rsc ws://localhost:8081/rsocket --route introduction --log --debug -d "{\"name\": \"Gigi\"}"`
	// fire-and-forget
	@MessageMapping("introduction")
	public Mono<Void> introduction(@Payload ClientMessage clientMessage){
		logger.debug("{}:  We have a new client -->  {}" , Instant.now(), clientMessage.getName());
		return Mono.empty();
	}

	@MessageMapping("check-service")
	public Mono<String> checkService(@Payload ClientMessage clientMessage){
		// test using: `rsc ws://localhost:8081/rsocket --route check-service --log --debug -d "{\"name\": \"Gigi\"}"`
		// request/response
		logger.debug("{}:  Ping request from client --> {}" , Instant.now(), clientMessage.getName());
		return Mono.just(Instant.now() + ": Service online. Send command.");
	}

	// request/stream
	// test using: `rsc ws://localhost:8081/rsocket --stream --route show-books --log --debug -d "{\"name\": \"Gigi\"}"`
	@MessageMapping("show-books")
	public Flux<Book> showBooks(@Payload ClientMessage clientMessage) {
		logger.debug("{}:  Random releases requested by client --> {}" , Instant.now(), clientMessage.getName());
		return Flux.fromStream(
				Stream.generate(BookNewReleasesUtil::randomRelease))
				.delayElements(Duration.ofSeconds(1L));
	}

	/*
	// not testable with rsc
	// request/response
	@MessageMapping("check-service")
	public Mono<String> checkService(@Payload Mono<ClientMessage> clientMessage){
		return clientMessage
				.doOnNext(message -> logger.debug("{}:  Ping request from client --> {}" , Instant.now(), message.getName()))
				.map( message -> Instant.now() + ": Service online. Send command.");
	}

	// not testable with rsc
	// request/stream
	@MessageMapping("show-books")
	public Flux<Book> showBooks(@Payload Mono<ClientMessage> clientMessage) {
		return clientMessage
				.doOnNext(message -> logger.debug("{}:  Random releases requested by client --> {}" , Instant.now(), message.getName()))
				.thenMany(Flux.fromStream(
						Stream.generate(BookNewReleasesUtil::randomRelease))
						.delayElements(Duration.ofSeconds(1L)));
	}
	*/


	//channel (bi-directional streams)
	@MessageMapping("books-channel")
	public Flux<Book> useChannel(@Payload Flux<ClientMessage> messages) {
		return messages
				.map(message-> BookNewReleasesUtil.randomForAuthor(message.getAuthor()))
				.delayElements(Duration.ofSeconds(1L));
	}


}
