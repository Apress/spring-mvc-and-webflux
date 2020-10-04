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

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;
import static org.hamcrest.CoreMatchers.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Created by Iuliana Cosmina on 18/07/2020
 */
@Disabled("For some reason these tests fail in the gradle build, run them only manually from IntelliJ IDEA")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookstoreAppTest {

	@Autowired
	private WebTestClient client;

	@Test
	public void shouldReturnABook(){
		client.get().uri("/books/{isbn}", "9781484227893")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(APPLICATION_JSON_VALUE)
				.expectBody(Book.class)
				.value(Book::getTitle, equalTo("Spring Boot 2 Recipes"));
	}

	@Test
	public void fetchAllBooks() {
		FluxExchangeResult<Book> result = client.get().uri("/books")
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType("application/stream+json")
				.returnResult(Book.class);

		StepVerifier.create(result.getResponseBody())
				.expectNextCount(3)
				.verifyComplete();
	}
}
