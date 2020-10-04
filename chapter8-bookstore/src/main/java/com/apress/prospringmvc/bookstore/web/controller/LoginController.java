package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.service.AccountService;
import com.apress.prospringmvc.bookstore.service.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Controller to handle login. 
 * 
 * @author Marten Deinum
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {

    public static final String ACCOUNT_ATTRIBUTE = "account";
    public static final String REQUESTED_URL = "REQUESTED_URL";

    @Autowired
    private AccountService accountService;

    @GetMapping
    public void login() {
    }

    @PostMapping
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpSession session)
            throws AuthenticationException {
        var account = this.accountService.login(username, password);
        session.setAttribute(ACCOUNT_ATTRIBUTE, account);
        var url = (String) session.getAttribute(REQUESTED_URL);
        session.removeAttribute(REQUESTED_URL); // Remove the attribute
        if (StringUtils.hasText(url) && !url.contains("login")) { // Prevent loops for the login page.
            return "redirect:" + url;
        } else {
            return "redirect:/index.htm";
        }
    }

}
