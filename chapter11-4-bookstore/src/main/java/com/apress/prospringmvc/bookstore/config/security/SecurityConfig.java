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

import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;

import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;

import java.net.URI;

/**
 * Created by Iuliana Cosmina on 04/08/2020
 */
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig  {

	@Bean
	SecurityWebFilterChain authorization(ServerHttpSecurity http) {

		final RedirectServerLogoutSuccessHandler logoutSuccessHandler = new RedirectServerLogoutSuccessHandler();
		logoutSuccessHandler.setLogoutSuccessUrl(URI.create("/"));

		return http
				.authorizeExchange(authorize -> authorize
						.matchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
						.pathMatchers("/book/edit/*", "/book/create").hasRole("ADMIN")
						.pathMatchers("/customer/edit/*").hasRole("ADMIN")
						.matchers(ServerWebExchangeMatchers.pathMatchers(HttpMethod.DELETE,
								"/book/delete/*", "/customer/delete/*", "/account/delete/*")).hasRole("ADMIN")
						.anyExchange().permitAll()
				)
				.formLogin(formLogin -> formLogin.loginPage("/login"))
				.logout(logoutSpec -> logoutSpec.logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler))
				//.csrf(csrf ->  csrf.csrfTokenRepository(repo()))
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.build();
	}

	/*@Bean
	public ServerCsrfTokenRepository repo() {
		WebSessionServerCsrfTokenRepository repo = new WebSessionServerCsrfTokenRepository();
		repo.setParameterName("_csrf");
		repo.setHeaderName("X-CSRF-TOKEN"); // default header name
		return repo;
	}*/

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringSecurityDialect securityDialect() {
		return new SpringSecurityDialect();
	}
}
