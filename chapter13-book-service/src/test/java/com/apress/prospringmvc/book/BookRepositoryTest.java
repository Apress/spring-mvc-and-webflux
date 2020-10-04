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
import com.apress.prospringmvc.book.repository.BookRepository;
import com.apress.prospringmvc.book.service.BookServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

/**
 * Created by Iuliana Cosmina on 28/06/2020
 */
@DataMongoTest
@Import({BookServiceImpl.class, BookHandler.class})
public class BookRepositoryTest {
	private final Logger logger = LoggerFactory.getLogger(BookRepositoryTest.class);

	@Autowired
	BookRepository bookRepository;

	@BeforeEach
	void setup() {
		var book1 = new Book("Dummy Book One", BigDecimal.valueOf(23.39), 1983, "Dum Dum", "1111484227893", "Dum");
		var book2 = new Book("Dummy Book Two", BigDecimal.valueOf(30.99), 1974, "Dim Dim", "1111484229999", "Dim");

		bookRepository.saveAll(Flux.just(book1, book2))
				.thenMany(bookRepository.findAll())
				.blockLast();  // accepted in a test context
	}

	@Test
	void testBooksIds() {
		 bookRepository.findAllLight().as(StepVerifier::create)
				 .expectNextCount(2)
				 .verifyComplete();
	}

	@Test
	void testFindByCategory() {
		bookRepository.findByCategory("Dum").as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testFindRandom(){
		PageRequest request = PageRequest.of(0, 1);
		bookRepository.findRandom(request)
				.log()
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testQuery(){
		BookSearchCriteria bookSearchCriteria  = new BookSearchCriteria();
		bookSearchCriteria.setCategory("Dim");
		Query query = new Query();
		if (bookSearchCriteria.getTitle() != null) {
			query.addCriteria(Criteria.where("title").is(bookSearchCriteria.getTitle()));
		}
		if (bookSearchCriteria.getCategory() != null) {
			query.addCriteria(Criteria.where("category").is(bookSearchCriteria.getCategory()));
		}

		bookRepository.findAll(query)
				.log()
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	@AfterEach
	void tearDown() {
		bookRepository.deleteAll()
				.subscribe(
						unused -> logger.info("Cleanup done!"),
						error -> logger.error("Cleanup failed! ", error)
				);
	}
}
