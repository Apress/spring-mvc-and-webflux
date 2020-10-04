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
package com.apress.prospringmvc.bookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Iuliana Cosmina on 18/07/2020
 */
@RestController
public class IndexController implements ApplicationContextAware {
	private final Logger logger = LoggerFactory.getLogger(IndexController.class);

	private ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path="/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<String> index(){
		return Mono.just("It works!");
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path="/debug", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Pair<String,String>> debug(ServerWebExchange exchange) {
		if(Objects.requireNonNull(exchange.getRequest().getHeaders().get("user-agent")).stream().anyMatch(v-> v.startsWith("curl"))){
			logger.debug("Development request with id: {}", exchange.getRequest().getId());
			ResponseCookie devCookie = ResponseCookie.from("Invoking.Environment.Cookie", "dev").maxAge(Duration.ofMinutes(5)).build();
			exchange.getResponse().addCookie(devCookie);
		}
		List<Pair<String,String>> info = new ArrayList<>();
		Arrays.stream(ctx.getBeanDefinitionNames()).forEach(beanName ->
			info.add(Pair.of(beanName, ctx.getBean(beanName).getClass().getName()))
		);
		return Flux.fromIterable(info).zipWith(Flux.interval(Duration.ofSeconds(1))).map(Tuple2::getT1);
	}
}
