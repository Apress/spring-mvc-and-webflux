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
package com.apress.prospringmvc.bookstore.config.security;

import com.apress.prospringmvc.bookstore.document.Account;
import com.apress.prospringmvc.bookstore.repository.AccountRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Created by Iuliana Cosmina on 06/08/2020
 */
@Service
public class ReactiveAuthenticationService implements ReactiveUserDetailsService {

	private final AccountRepository accountRepository;

	public ReactiveAuthenticationService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return accountRepository.findByUsername(username).switchIfEmpty(
				Mono.defer(() -> Mono.error(new UsernameNotFoundException("User Not Found"))
		)).map(this::toUserDetails);
	}

	private UserDetails toUserDetails(Account account) {
		String[] authorities = new String[account.getRoles().size()];
		authorities = account.getRoles().toArray(authorities);
		return User.withUsername(account.getUsername())
				.password(account.getPassword())
				.authorities(authorities)
				.build();
	}
}
