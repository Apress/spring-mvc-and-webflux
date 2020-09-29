package com.apress.prospringmvc.bookstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @RequestMapping(value = "/index.htm")
    public String indexPage(Model model) {
        model.addAttribute("theModelKey", "Spring says HI!");
        return "index";
    }
}
