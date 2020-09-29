package com.apress.prospringmvc.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

@SpringBootApplication
public class BookstoreApplication {

	public static void main(String... args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Bean
	public RouterFunction<ServerResponse> routes() {
		return route()
				.GET("/", response -> ok().render("index.html"))
				.build();

	}

}
