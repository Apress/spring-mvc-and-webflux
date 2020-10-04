package com.apress.prospringmvc.bookstore.service;

import com.apress.prospringmvc.bookstore.document.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Iuliana Cosmina on 27/07/2020
 */
public interface AccountService {

	Flux<Account> findAll();

	Mono<Account> findOne(String username);

	Mono<Void> update(String username, Account account);

	Mono<Account> create(Account account);

	Mono<Void> deleteByUsername(String username);

	/**
	 * Loading only data for Authentication and Authorization
	 * @param username
	 * @return
	 */
	Mono<Account> getAccountLight(String username);
}
