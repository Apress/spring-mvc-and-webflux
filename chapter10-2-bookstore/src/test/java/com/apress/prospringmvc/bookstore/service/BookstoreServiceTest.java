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
package com.apress.prospringmvc.bookstore.service;

import com.apress.prospringmvc.bookstore.document.*;
import com.apress.prospringmvc.bookstore.repository.AccountRepository;
import com.apress.prospringmvc.bookstore.repository.BookRepository;
import com.apress.prospringmvc.bookstore.service.BookstoreService;
import com.apress.prospringmvc.bookstore.service.BookstoreServiceImpl;
import com.apress.prospringmvc.bookstore.util.BookSearchCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Iuliana Cosmina on 27/07/2020
 */
@DataMongoTest
@Import(BookstoreServiceImpl.class)
public class BookstoreServiceTest {
	private final Logger logger = LoggerFactory.getLogger(BookstoreServiceTest.class);

	@Autowired BookRepository bookRepository;
	@Autowired AccountRepository accountRepository;
	@Autowired BookstoreService bookstoreService;

	@BeforeEach
	public void setup() {
		var book1 = new Book("Dummy Book One", BigDecimal.valueOf(23.39), 1983, "Dum Dum", "1111484227893", "Dum");
		var book2 = new Book("Dummy Book Two", BigDecimal.valueOf(30.99), 1974, "Dim Dim", "1111484229999", "Dim");
		var book3 = new Book("Dummy Book Three", BigDecimal.valueOf(50.99), 1942, "Dom Dom", "1111484228888", "Dim");

		Address address = new Address();
		address.setStreet("Test Street");
		address.setCity("Somewhere");
		address.setCountry("Space");

		Account test = new Account();
		test.setFirstName("test");
		test.setUsername("test");
		test.setLastName("test");
		test.setEmailAddress("test@doe.com");
		test.setPassword("test");
		test.setAddress(address);
		test.setRoles(List.of("ROLE_TEST"));

		Order order = new Order();
		order.setOrderDate(new Date());
		order.setShippingAddress(test.getAddress());
		order.setDeliveryDate(new Date());
		order.setBillingSameAsShipping(true);

		bookRepository.saveAll(Flux.just(book1, book2, book3))
				.thenMany(bookRepository.findAll())
				.thenMany(bookRepository.findByIsbn("1111484229999"))
				.doOnNext(
						book -> {
							OrderDetail orderDetail = new OrderDetail(book, 2);
							order.addOrderDetail(orderDetail);
							test.setOrders(List.of(order));
						}
				).then(accountRepository.save(test))
				.thenMany(accountRepository.findAll())
				.blockLast();  // accepted in a test context
	}

	@Test
	void testFindBooksByCategory(){
		bookstoreService.findBooksByCategory("Dim").log().as(StepVerifier::create)
				.expectNextCount(2)
				.verifyComplete();
	}

	@Test
	void testFindRandomBooks(){
		bookstoreService.findRandomBooks().log().as(StepVerifier::create)
				.expectNextCount(2)
				.verifyComplete();
	}

	@Test
	void testFindBooksNone(){
		bookstoreService.findBooks(new BookSearchCriteria()).log().as(StepVerifier::create)
				.verifyComplete();
	}

	@Test
	void testQueryForBooks(){
		BookSearchCriteria criteria = new BookSearchCriteria();
		criteria.setCategory("Dim");
		bookstoreService.findBooks(criteria).log().as(StepVerifier::create)
				.expectNextCount(2)
				.verifyComplete();
	}

	@AfterEach
	public void tearDown() {
		bookRepository.deleteAll()
				.thenMany(accountRepository.deleteAll())
				.subscribe(
						unused -> logger.info("Cleanup done!"),
						error -> logger.error("Cleanup failed! ", error)
				);
	}
}
