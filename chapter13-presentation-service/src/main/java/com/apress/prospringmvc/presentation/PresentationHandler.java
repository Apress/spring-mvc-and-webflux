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

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.thymeleaf.spring5.context.webflux.IReactiveSSEDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * Created by Iuliana Cosmina on 30/08/2020
 */
@Component
public class PresentationHandler {
	public static final String SPRING = "Spring";
	public static final String JAVA = "Java";
	public static final String WEB = "Web";

	private final PresentationService presentationService;

	public PresentationHandler(PresentationService presentationService) {
		this.presentationService = presentationService;
	}

	final HandlerFunction<ServerResponse> main = serverRequest -> ok()
			.contentType(MediaType.TEXT_HTML)
			.render("index");

	final HandlerFunction<ServerResponse> searchPage = serverRequest -> ok()
			.contentType(MediaType.TEXT_HTML)
			.render("book/search", Map.of(
					"categories", List.of(WEB, SPRING, JAVA),
					"bookSearchCriteria", new BookSearchCriteria()));

	public Mono<ServerResponse> retrieveResults(ServerRequest request) {
		var criteriaMono = request.bodyToMono(BookSearchCriteria.class);
		return criteriaMono.log()
				.flatMap(criteria -> ok().contentType(MediaType.APPLICATION_JSON)
						.body(presentationService.findBooks(criteria), Book.class));
	}

	final HandlerFunction<ServerResponse> checkoutPage = serverRequest -> ok()
			.contentType(MediaType.TEXT_HTML)
			.render("cart/checkout");

	final HandlerFunction<ServerResponse> loginPage = serverRequest -> ok()
			.contentType(MediaType.TEXT_HTML)
			.render("login");

	public Mono<ServerResponse> registerPage(ServerRequest request) {
		Locale currentLocale = LocaleContextHolder.getLocale();
		var account = new Account();
		account.getAddress().setCountry(currentLocale.getCountry());
		return ok().contentType(MediaType.TEXT_HTML)
				.render("customer/register", Map.of(
						"countries", countries(currentLocale),
						"account", account));
	}

	public Mono<ServerResponse>  randomFragment(ServerRequest request) {
		return ok().contentType(MediaType.APPLICATION_JSON).body(presentationService.randomBooks(), Book.class);
	}

	public Mono<ServerResponse>  newsFragment(ServerRequest request) {
		final IReactiveSSEDataDriverContextVariable dataDriver =
				new ReactiveDataDriverContextVariable(presentationService.techNews(),
						1, "techNews");

		return ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.render("book/search :: #techNews",  Map.of("techNews", dataDriver));
	}

	public Mono<ServerResponse>  releasesFragment(ServerRequest request) {
		final IReactiveSSEDataDriverContextVariable dataDriver =
				new ReactiveDataDriverContextVariable(presentationService.newReleases(),
						1, "newBooks");

		return ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.render("book/search :: #newBooks",  Map.of("newBooks", dataDriver));
	}

	private static Map<String, String> countries(Locale currentLocale) {
		var countries = new TreeMap<String, String>();
		for (var locale : Locale.getAvailableLocales()) {
			countries.put(locale.getCountry(), locale.getDisplayCountry(currentLocale));
		}
		return countries;
	}
}