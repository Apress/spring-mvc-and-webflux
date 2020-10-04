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

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Created by Iuliana Cosmina on 19/07/2020
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ReactiveServiceTest {

	private static final  Logger logger = LoggerFactory.getLogger(ReactiveServiceTest.class);
	@Autowired
	BookService bookService;

	@Test
	void shouldReadAllPersons() {
		bookService.findAll()
				.as(StepVerifier::create)
				.expectNextCount(3)
				.verifyComplete();
	}

	@Test
	void shouldReturnOne(){
		Book book = new Book("Spring Boot 2 Recipes", "Marten Deinum", "9781484227893", DataInitializer.Category.SPRING);
		bookService.findByIsbn("9781484227893").as(StepVerifier::create)
				.expectSubscription()
				.expectNext(book)
				.verifyComplete();
	}

	@Test
	void bogus(){
		Flux<String> items =  Flux.just("a", "b", "c");
		items.transform(s -> addLogging(s)).subscribe();
		//addLogging(items);
		//items.doOnNext(it -> System.out.printf("Received "+ it)). doOnError(e -> e.printStackTrace()).subscribe();
		items.subscribe();
	}

	static Flux<String>  addLogging(Flux<String> flux) {
		return flux.doOnNext(it -> System.out.printf("Received "+ it)). doOnError(e -> e.printStackTrace());
	}

}
