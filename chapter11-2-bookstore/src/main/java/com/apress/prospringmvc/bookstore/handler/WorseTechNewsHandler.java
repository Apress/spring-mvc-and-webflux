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
import reactor.core.publisher.SynchronousSink;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Iuliana Cosmina on 15/08/2020
 * @description: This is yet another bad WebSocket handler.It is supposed to store references to each stream that sends
 * messages to the client, but by providing the stream ourselves, Spring is not involved anymore, so no subscriber is
 * called to consume messages by sending them to the client.
 */
@BadHandler
public class WorseTechNewsHandler implements WebSocketHandler {
	private final Logger logger = LoggerFactory.getLogger(TechNewsHandler.class);

	private final ConcurrentHashMap<String, Flux<WebSocketMessage>> connections = new ConcurrentHashMap<>();

	private Flux<WebSocketMessage> getRandomNews(final String message, final WebSocketSession session) {
		long rate = "Slow down mate!".equals(message) ? 5L : 2L;
		return Flux
				.create(fluxSink -> {
					var publisher = connections.get(session.getId());
					if (publisher != null) {
						connections.get(session.getId()).subscribe().dispose();
						connections.remove(session.getId());
					}
					var data = Flux.generate((SynchronousSink<String> synchronousSink) ->
							synchronousSink.next(BookNewReleasesUtil.randomNews())).map(session::textMessage).delayElements(Duration.ofSeconds(rate));
					connections.put(session.getId(), data);
				});
	}

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		return session.send(session.receive()
				.map(WebSocketMessage::getPayloadAsText)
				.flatMap(message -> this.getRandomNews(message, session))
		).then();
	}
}
