package com.apress.prospringmvc.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class BookstoreApplication extends SpringBootServletInitializer {

	public static void main (String...args){
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure (SpringApplicationBuilder builder){
		return builder.sources(BookstoreApplication.class);
	}
}

