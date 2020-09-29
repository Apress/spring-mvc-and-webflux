package com.apress.prospringmvc.bookstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller shows the index page.
 * 
 * @author Marten Deinum
 */
@Controller
public class IndexController {

    @GetMapping(value = "/index.htm")
    public ModelAndView indexPage() {
        return new ModelAndView("index.html");
    }
}
