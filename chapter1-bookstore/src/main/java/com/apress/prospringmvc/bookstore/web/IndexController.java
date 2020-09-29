package com.apress.prospringmvc.bookstore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

	@GetMapping(value = "/index.htm")
	public ModelAndView indexPage() {
		var mav = new ModelAndView("/WEB-INF/views/index.jsp");
		mav.addObject("theModelKey", "Spring says HI!");
		return mav;
	}
}
