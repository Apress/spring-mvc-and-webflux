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
package com.apress.prospringmvc.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.IOException;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

/**
 * Created by Iuliana Cosmina on 30/08/2020
 */
@EnableEurekaClient
@SpringBootApplication
public class PresentationServiceApplication {

	private static Logger logger = LoggerFactory.getLogger(PresentationServiceApplication.class);

	public static void main(String... args) throws IOException {
		// Look for configuration in  presentation-service.properties or presentation-service.yml
		System.setProperty("spring.config.name", "presentation-service");
		var ctx = SpringApplication.run(PresentationServiceApplication.class, args);
		assert (ctx != null);
		logger.info("Started ...");
		System.in.read();
		ctx.close();
	}

	@LoadBalanced @Bean
	WebClient.Builder webClientBuilder() {
		return WebClient.builder();
	}

	@Bean
	public RouterFunction<ServerResponse> router(PresentationHandler handler){
		return RouterFunctions
				.route(GET("/"), handler.main) // curl  http://localhost:7000
				.andRoute(GET("/index.htm"), handler.main) // curl -H "Accept:text/event-stream" http://localhost:4000/techNews
				.andRoute(GET("/book/search"), handler.searchPage)
				.andRoute(POST("/book/search"), handler::retrieveResults)
				.andRoute(GET("/cart/checkout"), handler.checkoutPage)
				.andRoute(GET("/customer/register"), handler::registerPage)
				.andRoute(GET("/customer/login"), handler.loginPage)
				.andRoute(GET("/book/random"), handler::randomFragment) // curl -H "Accept:text/event-stream" http://localhost:6001/book/random
				.andRoute(GET("/tech/news"), handler::newsFragment) // curl -H "Accept:text/event-stream" http://localhost:4000/tech/news
				.andRoute(GET("/book/releases"), handler::releasesFragment) // curl -H "Accept:text/event-stream" http://localhost:5000/book/releases

				.filter((request, next) -> {
					logger.info("Before handler invocation: " + request.path());
					return next.handle(request);
				});
	}
}