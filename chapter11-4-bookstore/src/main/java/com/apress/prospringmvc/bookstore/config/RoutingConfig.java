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

import com.apress.prospringmvc.bookstore.handler.AccountHandler;
import com.apress.prospringmvc.bookstore.handler.BookHandler;
import com.apress.prospringmvc.bookstore.handler.IndexHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

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
	public RouterFunction<ServerResponse> bookRouter(IndexHandler indexHandler, BookHandler bookHandler, AccountHandler accountHandler) {

		return RouterFunctions
				.route(GET("/"), indexHandler::main)
				.andRoute(GET("/index.htm"), indexHandler::main)
				.andRoute(POST("/book/search"), bookHandler::search)
				.andRoute(GET("/book/random"), bookHandler.random)
				.andRoute(GET("/book/random-news"), bookHandler.releases)

				.andRoute(GET("/account"), accountHandler::list)
				.andRoute(PUT("/account/{username}"), accountHandler::update)
				.andRoute(POST("/account"), accountHandler::add)
				.andRoute(DELETE("/account{username}"), accountHandler.delete)

				.andRoute(GET("/book/by/{isbn}"), bookHandler::findOne)
				.andRoute(PUT("/book/edit/{isbn}"), bookHandler.update)
				.andRoute(POST("/book/create"), bookHandler::create)
				.andRoute(DELETE("/book/delete/{isbn}"), bookHandler.delete)

				.filter((request, next) -> {
					logger.info("Before handler invocation: " + request.path());
					var response = next.handle(request);
					logger.info("After handler invocation: " + request.path());
					return response;
				});
	}
}
