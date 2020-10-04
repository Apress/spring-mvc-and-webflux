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
package com.apress.prospringmvc.bookstore.util;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.adapter.DefaultServerWebExchange;
import org.springframework.web.server.adapter.HttpWebHandlerAdapter;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Locale;

import static org.springframework.util.StringUtils.isEmpty;

/**
 * Created by Jonathan Mendoza
 * A {@link WebFilter} that sets the 'Accept-Language' header from the value of a query parameter.
 * https://stackoverflow.com/users/6417169/jonatan-mendoza
 * Improved to add cookies support by Iuliana Cosmina
 */
@Component
public class LanguageQueryParameterWebFilter implements WebFilter {

	public static final String LOCALE_REQUEST_ATTRIBUTE_NAME = "Bookstore.Cookie.LOCALE";

	private final ApplicationContext applicationContext;

	private HttpWebHandlerAdapter httpWebHandlerAdapter;

	public LanguageQueryParameterWebFilter(final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void loadHttpHandler() {
		this.httpWebHandlerAdapter = applicationContext.getBean(HttpWebHandlerAdapter.class);
	}

	@Override
	public Mono<Void> filter(final ServerWebExchange exchange, final WebFilterChain chain) {
		final ServerHttpRequest request = exchange.getRequest();
		final MultiValueMap<String, String> queryParams = request.getQueryParams();
		final String languageValue = queryParams.getFirst("lang");

		final ServerWebExchange localizedExchange = getServerWebExchange(languageValue, exchange);
		return chain.filter(localizedExchange);
	}

	private ServerWebExchange getServerWebExchange(final String languageValue, final ServerWebExchange exchange) {
		return isEmpty(languageValue)
				? getLocaleFromCookie(exchange)
				: getLocalizedServerWebExchange(languageValue, exchange);
	}

	private ServerWebExchange getLocalizedServerWebExchange(final String languageValue, final ServerWebExchange exchange) {
		setLocaleToCookie(languageValue, exchange);
		final ServerHttpRequest httpRequest = exchange.getRequest()
				.mutate()
				.headers(httpHeaders -> httpHeaders.set("Accept-Language", languageValue))
				.build();

		return new DefaultServerWebExchange(httpRequest, exchange.getResponse(),
				httpWebHandlerAdapter.getSessionManager(), httpWebHandlerAdapter.getCodecConfigurer(),
				httpWebHandlerAdapter.getLocaleContextResolver());
	}

	private void setLocaleToCookie(String lang, ServerWebExchange exchange) {
		MultiValueMap<String, HttpCookie> cookies =  exchange.getRequest().getCookies();
		HttpCookie langCookie = cookies.getFirst(LOCALE_REQUEST_ATTRIBUTE_NAME);
		if(langCookie == null || !lang.equals(langCookie.getValue())) {
			ResponseCookie cookie = ResponseCookie.from(LOCALE_REQUEST_ATTRIBUTE_NAME, lang).maxAge(Duration.ofMinutes(5)).build();
			exchange.getResponse().addCookie(cookie);
		}
	}

	private ServerWebExchange getLocaleFromCookie(final ServerWebExchange exchange){
		MultiValueMap<String, HttpCookie> cookies =  exchange.getRequest().getCookies();
		HttpCookie langCookie = cookies.getFirst(LOCALE_REQUEST_ATTRIBUTE_NAME);
		String langValue = langCookie != null ? langCookie.getValue() : Locale.getDefault().getDisplayLanguage();

		final ServerHttpRequest httpRequest = exchange.getRequest()
				.mutate()
				.headers(httpHeaders -> httpHeaders.set("Accept-Language", langValue))
				.build();

		return new DefaultServerWebExchange(httpRequest, exchange.getResponse(),
				httpWebHandlerAdapter.getSessionManager(), httpWebHandlerAdapter.getCodecConfigurer(),
				httpWebHandlerAdapter.getLocaleContextResolver());
	}

}
