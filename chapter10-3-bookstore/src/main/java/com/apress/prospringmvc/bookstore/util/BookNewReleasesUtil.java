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
package com.apress.prospringmvc.bookstore.util;

import com.apress.prospringmvc.bookstore.document.Book;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * Created by Iuliana Cosmina on 29/07/2020
 */
public class BookNewReleasesUtil {

	private static final Random RANDOM = new Random(System.currentTimeMillis());

	public static final List<Book> NEW_BOOKS = List.of(
			new Book("Spring Boot 3 Recipes", BigDecimal.valueOf(38.44), 2022,"Marten Deinum", "9781484227899", Book.Category.SPRING),
			new Book("Spring WebFlux for Dummies", BigDecimal.valueOf(39.44), 2021,"Iuliana Cosmina", "9781484227888", Book.Category.SPRING),
			new Book("Reactive Java Recipes", BigDecimal.valueOf(49.44), 2022,"Iuliana Cosmina", "97814842278944", Book.Category.JAVA),
			new Book("JavaScript for the Backend Developer", BigDecimal.valueOf(51.44), 2020,"James Crook", "9781484227822", Book.Category.WEB),
			new Book("Pro Spring 6", BigDecimal.valueOf(59.44), 2022,"Iuliana Cosmina", "9781484227893", Book.Category.SPRING),
			new Book("Reactive Spring", BigDecimal.valueOf(25.44), 2020,"Josh Long", "9781484227111", Book.Category.SPRING),
			new Book("Spring MVC and WebFlux", BigDecimal.valueOf(50.44), 2020,"Marten Deinum & Iuliana Cosmina", "9781484227222", Book.Category.WEB)
	);

	public static Book randomRelease() {
		return NEW_BOOKS.get(RANDOM.nextInt(NEW_BOOKS.size()));
	}

	public static final List<String> TECH_NEWS = List.of(
			"Apress merged with Springer.",
			"VMWare buys Pivotal for a ridiculous amount of money!",
			"Twitter was hacked!",
			"Amazon launches reactive API for DynamoDB.",
			"Java 17 will be released in September 2021.",
			"The JavaScript world is still 'The Wild Wild West'.",
			"Java modules, still a topic that developers frown upon."
			);

	public static String randomNews() {
		return TECH_NEWS.get(RANDOM.nextInt(TECH_NEWS.size()));
	}


}
