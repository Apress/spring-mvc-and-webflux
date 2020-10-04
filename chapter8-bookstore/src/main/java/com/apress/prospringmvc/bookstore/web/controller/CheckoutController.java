package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.domain.Account;
import com.apress.prospringmvc.bookstore.domain.Cart;
import com.apress.prospringmvc.bookstore.domain.Order;
import com.apress.prospringmvc.bookstore.service.BookstoreService;
import com.apress.prospringmvc.bookstore.validation.OrderValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author Marten Deinum
 */
@Controller
@SessionAttributes(types = { Order.class })
@RequestMapping("/cart/checkout")
public class CheckoutController {

    private final Logger logger = LoggerFactory.getLogger(CheckoutController.class);

    @Autowired
    private Cart cart;

    @Autowired
    private BookstoreService bookstoreService;

    @ModelAttribute("countries")
    public Map<String, String> countries(Locale currentLocale) {
        var countries = new TreeMap<String, String>();
        for (var locale : Locale.getAvailableLocales()) {
            countries.put(locale.getCountry(), locale.getDisplayCountry(currentLocale));
        }
        return countries;
    }

    @GetMapping
    public void show(HttpSession session, Model model) {
        var account = (Account) session.getAttribute(LoginController.ACCOUNT_ATTRIBUTE);
        var order = this.bookstoreService.createOrder(this.cart, account);
        model.addAttribute(order);
    }

    @PostMapping(params = "order")
    public String checkout(SessionStatus status, @Validated @ModelAttribute Order order, BindingResult errors) {
        if (errors.hasErrors()) {
            return "cart/checkout";
        } else {
            this.bookstoreService.store(order);
            status.setComplete(); //remove order from session
            this.cart.clear(); // clear the cart
            return "redirect:/index.htm";
        }
    }

    @PostMapping(params = "update")
    public String update(@ModelAttribute Order order) {
        order.updateOrderDetails();
        return "cart/checkout";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new OrderValidator());
    }

}
