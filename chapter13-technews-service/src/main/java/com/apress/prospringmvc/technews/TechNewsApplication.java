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
package com.apress.prospringmvc.technews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;

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
public class TechNewsApplication {

	public static void main(String... args) {
		// Look for configuration in technews-service.properties or technews-service.yml
		System.setProperty("spring.config.name", "technews-service");
		SpringApplication springApplication = new SpringApplication(TechNewsApplication.class);
		springApplication.run(args);
	}

	@Bean
	public RouterFunction<ServerResponse> router(TechNewsHandler handler){
		return RouterFunctions
				.route(GET("/"), handler.main) // curl  http://localhost:4000
				.andRoute(GET("/index.htm"), handler.main) // curl -H "Accept:text/event-stream" http://localhost:4000/tech/news
				.andRoute(GET("/tech/news"), handler.data);
	}
}

@Component
class TechNewsHandler {
	private static final Random RANDOM = new Random(System.currentTimeMillis());

	public static final List<String> TECH_NEWS = List.of(
			"Apress merged with Springer.",
			"VMWare buys Pivotal.",
			"Twitter was hacked!",
			"Spring 6  is coming in December 2022.",
			"Amazon launches reactive API for DynamoDB.",
			"Java 17 will be released in September 2021.",
			"The JavaScript world is still 'The Wild Wild West'.",
			"Java modules, still a topic that developers frown upon."
	);

	public static String randomNews() {
		return TECH_NEWS.get(RANDOM.nextInt(TECH_NEWS.size()));
	}
		final HandlerFunction<ServerResponse> main = serverRequest -> ok()
				.contentType(MediaType.TEXT_HTML)
				.bodyValue("Tech News service up and running!");

		final HandlerFunction<ServerResponse> data = serverRequest -> ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(Flux.interval(Duration.ofSeconds(5)).map(delay -> randomNews()), String.class);
}


