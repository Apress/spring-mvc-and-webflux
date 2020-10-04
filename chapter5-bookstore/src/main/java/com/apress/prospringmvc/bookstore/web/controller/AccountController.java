package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.repository.AccountRepository;
import com.apress.prospringmvc.bookstore.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

@Controller
@RequestMapping("/customer/account")
@SessionAttributes(types = Account.class)
public class AccountController {

	private final AccountRepository accountRepository;
	private final OrderRepository orderRepository;

	public AccountController(AccountRepository accountRepository, OrderRepository orderRepository) {
		this.accountRepository = accountRepository;
		this.orderRepository = orderRepository;
	}

	@ModelAttribute("countries")
	public Map<String, String> countries(Locale currentLocale) {
		var countries = new TreeMap<String, String>();
		for (var locale : Locale.getAvailableLocales()) {
			countries.put(locale.getCountry(), locale.getDisplayCountry(currentLocale));
		}
		return countries;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setDisallowedFields("id");
		binder.setRequiredFields("username", "password", "emailAddress");
	}

	@GetMapping("/{accountId}")
	public String index(Model model, @PathVariable long accountId) {
		var account = this.accountRepository.findById(accountId).orElse(null);
		model.addAttribute(account);
		model.addAttribute("orders", this.orderRepository.findByAccount(account));
		return "customer/account";
	}

	@RequestMapping(value = "/{accountId}", method = {RequestMethod.POST, RequestMethod.PUT})
	public String update(@ModelAttribute Account account) {
		this.accountRepository.save(account);
		return "redirect:/customer/account/" + account.getId();
	}

}
