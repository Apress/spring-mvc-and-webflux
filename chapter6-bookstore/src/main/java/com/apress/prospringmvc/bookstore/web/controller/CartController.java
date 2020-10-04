package com.apress.prospringmvc.bookstore.web.controller;

import com.apress.prospringmvc.bookstore.domain.Book;
import com.apress.prospringmvc.bookstore.domain.Cart;
import com.apress.prospringmvc.bookstore.service.BookstoreService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author Marten Deinum
 */
@Controller
public class CartController {

	private final Cart cart;
	private final BookstoreService bookstoreService;

	public CartController(Cart cart, BookstoreService bookstoreService) {
		this.cart = cart;
		this.bookstoreService = bookstoreService;
	}

	@PostMapping("/cart/add/{bookId}")
	public String addToCart(@PathVariable("bookId") long bookId, @RequestHeader("referer") String referer) {
		var book = this.bookstoreService.findBook(bookId);
		this.cart.addBook(book);
		return "redirect:" + referer;
	}
}
