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
package com.apress.prospringmvc.newreleases;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Random;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import static org.springframework.web.reactive.function.server.ServerResponse.*;
/**
 * Created by Iuliana Cosmina on 29/08/2020
 */
@SpringBootApplication
@EnableEurekaClient
public class NewReleasesApplication {

	public static void main(String... args) {
		if (args.length == 1) {
			System.setProperty("server.port", args[0]);
		}
		// Look for configuration in newreleases-service.properties or newreleases-service.yml
		System.setProperty("spring.config.name", "newreleases-service");
		SpringApplication springApplication = new SpringApplication(NewReleasesApplication.class);
		springApplication.run(args);
	}

	@Bean
	public RouterFunction<ServerResponse> router(NewReleasesHandler handler){
		return RouterFunctions
				.route(GET("/"), handler.main) // curl  http://localhost:5000
				.andRoute(GET("/index.htm"), handler.main)
				.andRoute(GET("/book/releases"), handler.data); // curl -H "Accept:text/event-stream" http://localhost:5000/book/releases
	}
}

@Component
class NewReleasesHandler {
	private static final Random RANDOM = new Random(System.currentTimeMillis());

	public static final List<Book> NEW_BOOKS = List.of(
			new Book("Spring Boot 3 Recipes", 2022,"Marten Deinum"),
			new Book("Spring WebFlux for Dummies",  2021,"Iuliana Cosmina"),
			new Book("Reactive Java Recipes",  2022,"Iuliana Cosmina"),
			new Book("JavaScript for the Backend Developer",  2020,"James Crook"),
			new Book("Pro Spring 6", 2022,"Iuliana Cosmina"),
			new Book("Reactive Spring",  2020,"Josh Long"),
			new Book("Spring MVC and WebFlux",  2020,"Marten Deinum & Iuliana Cosmina")
	);

	public static Book randomRelease() {
		return NEW_BOOKS.get(RANDOM.nextInt(NEW_BOOKS.size()));
	}


	final HandlerFunction<ServerResponse> main = serverRequest -> ok()
				.contentType(MediaType.TEXT_HTML)
				.bodyValue("New Book Releases service up and running!");

		final HandlerFunction<ServerResponse> data = serverRequest -> ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(Flux.interval(Duration.ofSeconds(5)).map(delay -> randomRelease()), Book.class);
}


