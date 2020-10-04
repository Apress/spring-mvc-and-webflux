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
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;

/**
 * Created by Iuliana Cosmina on 19/07/2020
 */
@Service
public class BookServiceImpl implements  BookService {
	private static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

	private final BookRepository bookRepository;

	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public Mono<Book> findByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	@Override
	public Flux<Book> findAll() {
		return bookRepository.findAll();
				//.zipWith(Flux.interval(Duration.ofSeconds(2))).map(Tuple2::getT1);
	}

	@Override
	public Mono<Book> save(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public Mono<Void> update(String id, Mono<Book> bookMono) {
		return bookRepository.findById(id).doOnNext(original -> bookMono.doOnNext( updatedBook -> {
			original.setIsbn(updatedBook.getIsbn());
			original.setAuthor(updatedBook.getAuthor());
			original.setTitle(updatedBook.getTitle());
			original.setCategory(updatedBook.getCategory());
			bookRepository.save(original);
		})
		).then(Mono.empty());
	}

	@Override
	public Mono<Void> delete(String id) {
		return bookRepository.deleteById(id);
	}
}
