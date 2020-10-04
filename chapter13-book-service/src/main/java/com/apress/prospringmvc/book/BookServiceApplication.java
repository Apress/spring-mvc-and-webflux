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
package com.apress.prospringmvc.book;

import com.apress.prospringmvc.book.handler.BookHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;


/**
 * Created by Iuliana Cosmina on 29/08/2020
 */
@EnableEurekaClient
@SpringBootApplication
public class BookServiceApplication {

	public static void main(String... args) {
		// Look for configuration in book-service.properties or book-service.yml
		System.setProperty("spring.config.name", "book-service");
		SpringApplication springApplication = new SpringApplication(BookServiceApplication.class);
		springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
		springApplication.run(args);
	}

	@Bean
	public RouterFunction<ServerResponse> router(BookHandler handler){
		return RouterFunctions
				.route(GET("/"), handler.main) // curl  http://localhost:6001
				.andRoute(GET("/index.htm"), handler.main)
				.andRoute(GET("/book/random"), handler.random)
				.andRoute(POST("/book/search"), handler::search)
				.andRoute(GET("/book/by/{isbn}"), handler::findOne)
				.andRoute(PUT("/book/update/{isbn}"), handler.update)
				.andRoute(POST("/book/create"), handler::create)
				.andRoute(DELETE("/book/delete/{isbn}"), handler.delete);
	}
}
