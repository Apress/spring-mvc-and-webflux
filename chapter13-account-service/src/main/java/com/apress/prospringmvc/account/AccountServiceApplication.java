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
package com.apress.prospringmvc.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;

/**
 * Created by Iuliana Cosmina on 29/08/2020
 */
@SpringBootApplication
@EnableEurekaClient
public class AccountServiceApplication {

	public static void main(String... args) {
		// Look for configuration in account-service.properties or account-service.yml
		System.setProperty("spring.config.name", "account-service");
		SpringApplication springApplication = new SpringApplication(AccountServiceApplication.class);
		springApplication.run(args);
	}

	@Bean
	public RouterFunction<ServerResponse> router(AccountHandler handler){
		return RouterFunctions
				.route(GET("/"), handler.main) // curl  http://localhost:6002
				.andRoute(GET("/index.htm"), handler.main)
				.andRoute(GET("/account"), handler::list) // curl -H "Accept:text/event-stream" http://localhost:6002/account
				.andRoute(GET("/account/{username}"), handler::findOne)
				.andRoute(PUT("/account/{username}"), handler::update)
				.andRoute(POST("/account"), handler::add)
				.andRoute(DELETE("/account{username}"), handler.delete);
	}
}



