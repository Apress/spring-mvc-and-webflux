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

import com.apress.prospringmvc.bookstore.util.BadHandler;
import com.apress.prospringmvc.bookstore.util.BookNewReleasesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.stream.Stream;

/**
 * @author Iuliana Cosmina
 * @date 09/08/2020
 * @description: This sends messages to the client using an infinite stream. It just prints the messages received from the client.
 */
@BadHandler
public class TechNewsHandler implements WebSocketHandler {
	private final Logger logger = LoggerFactory.getLogger(TechNewsHandler.class);

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		var inbound = session.receive()
				.map(WebSocketMessage::getPayloadAsText)
				.doOnNext(message -> logger.debug("Client says: {}", message))
				.then();

		var source =  Flux.fromStream(Stream.generate(BookNewReleasesUtil::randomNews));

		var outbound = session.send(source.map(session::textMessage).delayElements(Duration.ofSeconds(2L)));

		return Mono.zip(inbound, outbound).then();
	}
}
