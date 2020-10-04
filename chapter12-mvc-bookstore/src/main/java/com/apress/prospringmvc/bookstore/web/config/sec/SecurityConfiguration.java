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
package com.apress.prospringmvc.bookstore.web.config.sec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * @author Iuliana Cosmina
 * @date 07/06/2020
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true/*, securedEnabled = true, jsr250Enabled = true*/)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/images/**", "/styles/**");
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(bookstoreAuthenticationProvider());
	}

	@Bean
	public AuthenticationProvider bookstoreAuthenticationProvider() {
		return new BookstoreAuthenticationProvider();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests(
						authorize -> authorize.mvcMatchers("/*/edit/*", "/*/delete/*", "/h2-console/**")
								.hasRole("ADMIN")
							  .anyRequest().permitAll()
				).formLogin(
						formLogin -> formLogin.loginPage("/login")
								.failureUrl("/login?auth_error=1")
				).logout(Customizer.withDefaults())
				//.csrf().disable();  //to access the H2-console
				.csrf().csrfTokenRepository(repo())
				.ignoringAntMatchers("/h2-console/**"); //don't apply CSRF protection to /h2-console
	}

	@Bean
	public CsrfTokenRepository repo() {
		HttpSessionCsrfTokenRepository repo = new HttpSessionCsrfTokenRepository();
		repo.setParameterName("_csrf");
		repo.setHeaderName("X-CSRF-TOKEN"); // default header name
		return repo;
	}
}