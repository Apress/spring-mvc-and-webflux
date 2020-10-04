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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Iuliana Cosmina on 12/07/2020
 */
@Component
public class DataInitializer {

	public static class Category {
		public static final String SPRING = "Spring";
		public static final String JAVA = "Java";
		public static final String WEB = "Web";
	}

	@Autowired ReactiveMongoOperations operations;

	private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

	private final BookRepository bookRepo;

	public DataInitializer(BookRepository bookRepo) {
		this.bookRepo = bookRepo;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		  operations.collectionExists(Book.class)
				.flatMap(exists -> exists ? operations.dropCollection(Book.class) : Mono.just(exists))
				.then(operations.createCollection(Book.class, CollectionOptions.empty())).subscribe(
					data -> logger.info("Collection saved: {}" , data ),
					error -> logger.info("Opps!"),
					() -> logger.info("Collection initialized!")
					);

		logger.info(" -->> Starting collection initialization...");
		bookRepo.saveAll(	Flux.just(
				new Book("Spring Boot 2 Recipes", "Marten Deinum", "9781484227893", Category.SPRING),
				new Book("Pivotal Certified Professional Core Spring 5 Developer Exam",  "Iuliana Cosmina", "9781484251355", Category.SPRING),
				new Book("Java for Absolute Beginners",  "Iuliana Cosmina", "9781484237779", Category.JAVA)
		)).subscribe(
				data -> logger.info("Saved {} books", data),
				error -> logger.error("Oops!"),
				() -> logger.info("Collection initialized!")
		);
	}
}
