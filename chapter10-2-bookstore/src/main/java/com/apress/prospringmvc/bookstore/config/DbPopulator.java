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
package com.apress.prospringmvc.bookstore.config;

import com.apress.prospringmvc.bookstore.document.Account;
import com.apress.prospringmvc.bookstore.document.Address;
import com.apress.prospringmvc.bookstore.document.Book;
import com.apress.prospringmvc.bookstore.repository.AccountRepository;
import com.apress.prospringmvc.bookstore.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import static com.apress.prospringmvc.bookstore.document.Book.Category;
import static com.apress.prospringmvc.bookstore.document.Account.Authority;

/**
 * Created by Iuliana Cosmina on 28/06/2020
 */
@Component
public class DbPopulator {

	private final Logger logger = LoggerFactory.getLogger(DbPopulator.class);

	//private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private final BookRepository bookRepository;
	private final AccountRepository accountRepository;

	public DbPopulator(BookRepository bookRepository, AccountRepository accountRepository) {
		this.bookRepository = bookRepository;
		this.accountRepository = accountRepository;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init(){
		List<Book> books = List.of(
				new Book("Spring Boot 2 Recipes", BigDecimal.valueOf(37.44), 2017,"Marten Deinum", "9781484227893", Category.SPRING),
				new Book("Pivotal Certified Professional Core Spring 5 Developer Exam", BigDecimal.valueOf(54.99), 2018,"Iuliana Cosmina", "9781484251355", Category.SPRING),
				new Book("Java for Absolute Beginners", BigDecimal.valueOf(24.99), 2020,"Iuliana Cosmina", "9781484237779", Category.JAVA),
				new Book("Java for Absolute Beginners, 2nd Edition", BigDecimal.valueOf(54.99), 2021,"Iuliana Cosmina", "9781484230042", Category.JAVA)
		);

		bookRepository.deleteAll().thenMany(
				bookRepository.saveAll(books))
				.thenMany(bookRepository.findAll())
				.subscribe(
						data -> logger.info("found books: {}", bookRepository),
						error -> logger.error("" + error),
						() -> logger.info(" -->> done books initialization...")
				);

		Address address = new Address();

		address.setStreet("Liberty Street");
		address.setCity("of angels");
		address.setCountry("Europe");

		Account john = new Account();
		john.setFirstName("john");
		john.setUsername("john");
		john.setLastName("doe");
		john.setEmailAddress("john@doe.com");
		john.setPassword("doe");
		//john.setPassword(passwordEncoder.encode("doe"));
		john.setAddress(address);
		john.setRoles(List.of(Authority.ROLE_USER));

		Account jane = new Account();
		jane.setFirstName("jane");
		jane.setLastName("doe");
		jane.setUsername("jane");
		jane.setEmailAddress("jane@doe.com");
		jane.setPassword("doe");
		//jane.setPassword(passwordEncoder.encode("doe"));
		jane.setAddress(address);
		jane.setRoles(List.of(Authority.ROLE_USER, Authority.ROLE_ADMIN));

		Account admin = new Account();
		admin.setFirstName("admin");
		admin.setLastName("admin");
		admin.setUsername("admin");
		admin.setEmailAddress("admin@admin.com");
		admin.setPassword("admin");
		//admin.setPassword(passwordEncoder.encode("admin"));
		admin.setAddress(address);
		admin.setRoles(List.of(Authority.ROLE_ADMIN));

		accountRepository.deleteAll().thenMany(
				accountRepository.saveAll(List.of(john, jane, admin)))
				.thenMany(accountRepository.findAll())
				.subscribe(
						data -> logger.info("found accounts: {}", accountRepository),
						error -> logger.error("" + error),
						() -> logger.info(" -->> done accounts initialization...")
				);
	}

}
