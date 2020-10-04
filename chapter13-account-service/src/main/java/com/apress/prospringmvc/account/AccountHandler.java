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
import com.apress.prospringmvc.account.service.AccountService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Created by Iuliana Cosmina on 04/08/2020
 */
@Component
public class AccountHandler {

	private final AccountService accountService;
	public final HandlerFunction<ServerResponse> delete;

	public AccountHandler(AccountService accountService) {
		this.accountService = accountService;
		delete = serverRequest -> ServerResponse.noContent()
				.build(accountService.deleteByUsername(serverRequest.pathVariable("username")));
	}

	public final HandlerFunction<ServerResponse> main = serverRequest -> ok()
			.contentType(MediaType.TEXT_HTML)
			.bodyValue("Account service up and running!");

	public Mono<ServerResponse> list(ServerRequest serverRequest){
		return ok()
				.contentType(MediaType.TEXT_EVENT_STREAM)
				.body(accountService.findAll(), Account.class);
	}

	public Mono<ServerResponse> add(ServerRequest serverRequest){
		return serverRequest.bodyToMono(Account.class)
		.flatMap(accountService::create)
				.flatMap(account -> ServerResponse.created(URI.create("/account/" + account.getUsername()))
						.contentType(MediaType.APPLICATION_JSON).bodyValue(account))
				.onErrorResume(error -> ServerResponse.badRequest().bodyValue(error));
	}

	public Mono<ServerResponse> update(ServerRequest serverRequest) {
	return	serverRequest.bodyToMono(Account.class)
				.flatMap(account -> accountService.update(serverRequest.pathVariable("username"), account))
				.then(noContent().build())
			.onErrorResume(error -> ServerResponse.badRequest().bodyValue(error));
	}

	public Mono<ServerResponse> findOne(ServerRequest request) {
		Mono<Account> accountMono = accountService.findOne(request.pathVariable("username")).log();
		return accountMono
				.flatMap(account -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(account))
				.switchIfEmpty(ServerResponse.notFound().build());
	}
}
