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
package com.apress.prospringmvc.bookstore.config;

import com.apress.prospringmvc.bookstore.handler.BookHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.server.PathContainer;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.function.server.support.ServerRequestWrapper;

import java.net.URI;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

/**
 * Created by Iuliana Cosmina on 27/07/2020
 */
@Configuration
public class RoutingConfig {
	private static Logger logger = LoggerFactory.getLogger(RoutingConfig.class);

	@Bean
	public RouterFunction<ServerResponse> staticRouter() {
		return RouterFunctions
				.resources("/static/**", new ClassPathResource("static/"));
	}

	@Bean
	public RouterFunction<ServerResponse> bookRouter(BookHandler bookHandler) {

		return RouterFunctions
				.route(i(POST("/book/search")), bookHandler::search)
				.andRoute(i(GET("/book/detail")), bookHandler::findOne)
				.andRoute(i(GET("/book/random")), bookHandler.random)

				.andRoute(i(GET("/book/isbn/{isbn}")), bookHandler::findOne)
				.andRoute(i(PUT("/book/isbn/{isbn}")), bookHandler.update)
				.andRoute(i(POST("/book/isbn")), bookHandler::create)
				.andRoute(i(DELETE("/book/isbn/{isbn}")), bookHandler.delete)
				.filter((request, next) -> {
					logger.info("Before handler invocation: " + request.path());
					return next.handle(request);
				});
	}

	/**
	 * Making URLs case insensitive using a RequestPredicate, applies only to functional endpoints
	 * @param target
	 * @return
	 */
	private static RequestPredicate i(RequestPredicate target) {
		return new CaseInsensitiveRequestPredicate(target);
	}
}

class CaseInsensitiveRequestPredicate implements RequestPredicate {

	private final RequestPredicate target;

	CaseInsensitiveRequestPredicate(RequestPredicate target) {
		this.target = target;
	}

	@Override
	public boolean test(ServerRequest request) {
		return this.target.test(new LowerCaseUriServerRequestWrapper(request));
	}

	@Override
	public String toString() {
		return this.target.toString();
	}
}


class LowerCaseUriServerRequestWrapper extends ServerRequestWrapper {

	LowerCaseUriServerRequestWrapper(ServerRequest delegate) {
		super(delegate);
	}

	@Override
	public URI uri() {
		return URI.create(super.uri().toString().toLowerCase());
	}

	@Override
	public String path() {
		return uri().getRawPath();
	}

	@Override
	public PathContainer pathContainer() {
		return PathContainer.parsePath(path());
	}
}
