package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.service.AccountService;
import com.apress.prospringmvc.bookstore.service.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * Controller to handle login.
 *
 * @author Marten Deinum
 */
@Controller
@RequestMapping("/login")
public class LoginController {

	private static final String ACCOUNT_ATTRIBUTE = "account";

	private final AccountService accountService;

	public LoginController(AccountService accountService) {
		this.accountService = accountService;
	}

	@GetMapping
	public String login() {
		return "login";
	}

	@PostMapping
	public String handleLogin(@RequestParam String username, @RequestParam String password,
														RedirectAttributes redirect, HttpSession session) {
		try {
			var account = this.accountService.login(username, password);
			session.setAttribute(ACCOUNT_ATTRIBUTE, account);
			return "redirect:/index.htm";
		} catch (AuthenticationException ae) {
			redirect.addFlashAttribute("exception", ae);
			return "redirect:/login";
		}
	}
}
