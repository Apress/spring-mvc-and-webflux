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
package com.apress.prospringmvc.bookstore.web;

import com.apress.prospringmvc.bookstore.util.BookNewReleasesUtil;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.stream.Stream;

/**
 * Created by Iuliana Cosmina on 16/08/2020
 */
public class TechNewsFluxTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(TechNewsFluxTest.class);

	@Test
	void testBackpressureHandlingOne() {
		var techNews = Flux.fromStream(Stream.generate(BookNewReleasesUtil::randomNews)).take(20).log();

		techNews.subscribe(new BaseSubscriber<>() {
			int processed;
			final int limit = 5;

			@Override
			protected void hookOnSubscribe(Subscription subscription) {
				subscription.request(limit);
			}

			@Override
			protected void hookOnNext(String news) {
				//client logic here
				if (++processed >= limit) {
					processed = 0;
					request(limit);
				}
			}
		});
	}


	@Test
	void testBackpressureHandlingTwo() {
		var techNews = Flux.fromStream(Stream.generate(BookNewReleasesUtil::randomNews)).take(20).log();
		consume(techNews.limitRate(5));
	}

	private void consume(Flux<String> input) {
		input.subscribe(/*s -> clientLogicHere(s)*/);
	}
}
