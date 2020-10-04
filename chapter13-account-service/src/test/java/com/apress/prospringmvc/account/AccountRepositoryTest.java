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
package com.apress.prospringmvc.account;

import com.apress.prospringmvc.account.document.Account;
import com.apress.prospringmvc.account.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * Created by Iuliana Cosmina on 30/08/2020
 */
@Disabled // comment this to run the test outside the gradle build
@DataMongoTest
@Import({AccountServiceImpl.class, AccountHandler.class})
public class AccountRepositoryTest {

	@Autowired
	AccountRepository accountRepository;

	@BeforeEach
	void setup(){
		var account1 = new Account("test", "one", "one@test.com", "testone", "test" );
		var account2 = new Account("test", "two", "two@test.com", "testtwo", "test" );

		accountRepository.saveAll(Flux.just(account1, account2))
				.thenMany(accountRepository.findAll())
				.blockLast();  // accepted in a test context
	}

	@Test
	void testFindAll(){
		accountRepository.findAll().as(StepVerifier::create)
				.expectNextCount(2)
				.verifyComplete();
	}

	@Test
	void testFindByUsername(){
		accountRepository.findByUsername("testone").as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testFindLightByUsername(){
		accountRepository.findLightByUsername("testtwo").as(StepVerifier::create)
				.expectNextMatches(account -> account.getFirstName() == null)
				.verifyComplete();
	}

}
