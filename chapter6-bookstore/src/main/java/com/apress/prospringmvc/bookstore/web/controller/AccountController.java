package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.repository.AccountRepository;
import com.apress.prospringmvc.bookstore.repository.OrderRepository;
import com.apress.prospringmvc.bookstore.web.method.support.SessionAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 *  
 * @author Marten Deinum

 *
 */
@Controller
@RequestMapping("/customer/account")
@SessionAttributes(types = Account.class)
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrderRepository orderRepository;

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

    @GetMapping
    public String index(Model model,
            @SessionAttribute(value = LoginController.ACCOUNT_ATTRIBUTE, exposeAsModelAttribute = true) Account account) {
        model.addAttribute("orders", this.orderRepository.findByAccount(account));
        return "customer/account";
    }

    @RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT })
    public String update(@ModelAttribute Account account) {
        this.accountRepository.save(account);
        return "redirect:/customer/account";
    }

}
