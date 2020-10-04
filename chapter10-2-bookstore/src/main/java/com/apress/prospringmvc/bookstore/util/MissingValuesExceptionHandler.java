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

import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by Iuliana Cosmina on 25/08/2020
 */
@Component
@Order(-2)
public class MissingValuesExceptionHandler implements WebExceptionHandler {

	@Override
	public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
		DataBuffer buffer;
		if (ex instanceof MissingValueException) {
			exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
			exchange.getResponse().getHeaders().add("Content-Type", "application/json");
			final String message = " {\"missing_value_for\": \""+ ((MissingValueException)ex).getFieldNames() +"\"}";
			buffer = exchange.getResponse().bufferFactory().wrap(message.getBytes());
		} else {
			exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
			exchange.getResponse().getHeaders().add("Content-Type", "application/json");
			buffer = exchange.getResponse().bufferFactory().wrap("Ooops!".getBytes());
		}
		return exchange.getResponse().writeWith(Flux.just(buffer));
	}
}
