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
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * Created by Iuliana Cosmina on 30/08/2020
 */
@Service
public class PresentationService {

	private static Logger logger = LoggerFactory.getLogger(PresentationService.class);

	private static final String BOOK_SERVICE_URL = "http://book-service";

	private static final String ACCOUNT_SERVICE_URL = "http://account-service";

	private static final String TECHNEWS_SERVICE_URL = "http://technews-service";

	private static final String NEWRELEASES_SERVICE_URL = "http://newreleases-service";

	private WebClient.Builder webClientBuilder;

	public PresentationService(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
	}

	public Flux<Book>  randomBooks() {
		return webClientBuilder.baseUrl(BOOK_SERVICE_URL).build()
				.get().uri("/book/random")
				.retrieve()
				.bodyToFlux(Book.class).map(book -> {
					logger.debug("Retrieved book: {}", book);
					return book;
				});
	}

	public Flux<Book>  findBooks(BookSearchCriteria criteria) {
		return webClientBuilder.baseUrl(BOOK_SERVICE_URL).build()
				.post().uri("/book/search")
				.bodyValue(criteria)
				.retrieve()
				.bodyToFlux(Book.class).log();
	}

	public Flux<Book> newReleases() {
		return webClientBuilder.baseUrl(NEWRELEASES_SERVICE_URL).build()
				.get().uri("/book/releases")
				.retrieve()
				.bodyToFlux(Book.class).map(book -> {
					logger.debug("Retrieved book: {}", book);
					return book;
				});
	}

	public Flux<String> techNews() {
		return webClientBuilder.baseUrl(TECHNEWS_SERVICE_URL).build()
				.get().uri("/tech/news")
				.retrieve()
				.bodyToFlux(String.class).map(val -> {
					logger.debug("Retrieved val : {}", val);
					return val;
				});
	}
}
