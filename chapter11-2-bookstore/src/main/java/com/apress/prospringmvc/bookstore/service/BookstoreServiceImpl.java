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
import com.apress.prospringmvc.bookstore.util.BookNewReleasesUtil;
import com.apress.prospringmvc.bookstore.util.BookSearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Iuliana Cosmina on 27/07/2020
 */
@Service
@Transactional(readOnly = true)
public class BookstoreServiceImpl implements  BookstoreService {
	private static final int RANDOM_BOOKS = 2;

	private final BookRepository bookRepository;
	private final AccountRepository accountRepository;

	public BookstoreServiceImpl(BookRepository bookRepository, AccountRepository accountRepository) {
		this.bookRepository = bookRepository;
		this.accountRepository = accountRepository;
	}

	@Override
	public Flux<Book> findBooksByCategory(String category) {
		return this.bookRepository.findByCategory(category);
	}

	@Override
	public Mono<Book> findBook(String id) {
		return this.bookRepository.findById(id);
	}

	@Override
	public Mono<Book> findBookByIsbn(String isbn) {
		return this.bookRepository.findByIsbn(isbn);
	}

	@Override
	public Flux<Book> findRandomBooks() {
		PageRequest request = PageRequest.of(0, RANDOM_BOOKS);
		return this.bookRepository.findRandom(request);
	}

	@Override
	public Mono<Order> findOrder(String id) {
		// TODO not sure if this is needed anymore
		return null;
	}

	@Override
	public Mono<List<Order>> findOrdersForAccountId(String accountId) {
		return this.accountRepository.findById(accountId).map(Account::getOrders);
	}

	@Override
	public Flux<Book> findBooks(BookSearchCriteria bookSearchCriteria) {
		if(bookSearchCriteria.isEmpty()) {
			return Flux.empty();
		}
		Query query = new Query();
		if (StringUtils.isNotEmpty(bookSearchCriteria.getTitle())) {
			query.addCriteria(Criteria.where("title").is(bookSearchCriteria.getTitle()));
		}
		if (StringUtils.isNotEmpty(bookSearchCriteria.getCategory())) {
			query.addCriteria(Criteria.where("category").is(bookSearchCriteria.getCategory()));
		}
		return bookRepository.findAll(query);
	}

	@Override
	public Mono<Order> createOrder(Cart cart, Account customer) {
		var order = new Order();
		for (Map.Entry<Book, Integer> line : cart.getBooks().entrySet()) {
			order.addOrderDetail(new OrderDetail(line.getKey(), line.getValue()));
		}
		order.setId(UUID.randomUUID().toString());
		order.setOrderDate(new Date());
		customer.addOrder(order);
		return Mono.just(order);
	}

	@Override
	public Mono<Book> addBook(Book book) {
		return this.bookRepository.save(book);
	}

	@Override
	public Mono<Void> deleteBook(String bookIsbn) {
		return bookRepository.findByIsbn(bookIsbn).doOnNext(bookRepository::delete).then(Mono.empty());
	}

	@Override
	public Flux<Book> randomBookNews() {
		return Flux.interval(Duration.ofSeconds(5)).map(delay -> BookNewReleasesUtil.randomRelease());
	}
}
