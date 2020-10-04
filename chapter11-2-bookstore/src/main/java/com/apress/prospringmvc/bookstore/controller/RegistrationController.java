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
package com.apress.prospringmvc.bookstore.controller;

import com.apress.prospringmvc.bookstore.document.Account;
import com.apress.prospringmvc.bookstore.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Iuliana Cosmina on 28/07/2020
 */
@Controller
@RequestMapping("/customer/register")
public class RegistrationController {
	private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

	private final AccountService accountService;

	public RegistrationController(AccountService accountService) {
		this.accountService = accountService;
	}

	@ModelAttribute("countries")
	public Map<String, String> countries(Locale currentLocale) {
		var countries = new TreeMap<String, String>();
		for (var locale : Locale.getAvailableLocales()) {
			countries.put(locale.getCountry(), locale.getDisplayCountry(currentLocale));
		}
		return countries;
	}

	@GetMapping
	@ModelAttribute
	public Account register(Locale currentLocale) {
		var account = new Account();
		account.getAddress().setCountry(currentLocale.getCountry());
		return account;
	}

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT })
	public String handleRegistration(@Valid @ModelAttribute Account account, BindingResult result) {
		if (result.hasErrors()) {
			return "customer/register";
		}
		this.accountService.create(account).doOnNext(acc ->  logger.debug("Account saved"))
				.then(Mono.empty()).subscribe();
		return "redirect:/login";
	}

}
