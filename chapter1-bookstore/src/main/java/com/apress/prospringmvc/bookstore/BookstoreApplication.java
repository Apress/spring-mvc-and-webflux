package com.apress.prospringmvc.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * {@code SpringBootServletInitializer} that will be called by Spring's {@code SpringServletContainerInitializer} as part
 * of the JEE {@code ServletContainerInitializer} pattern. This class will be called on application startup and will
 * configure our Spring Boot Application.
 * <p/>
 * 
 * It will first initializes our {@code AnnotationConfigWebApplicationContext} with the common {@link org.springframework.context.annotation.Configuration}
 * classes: {@code InfrastructureContextConfiguration} and {@code TestDataContextConfiguration} using a typical JEE
 * {@code ContextLoaderListener}.
 * <p/>
 * 
 * Next it creates a {@link org.springframework.web.servlet.DispatcherServlet}, being a normal JEE Servlet which will create on its turn a child
 * {@code AnnotationConfigWebApplicationContext} configured with the Spring MVC {@code Configuration} classes
 * {@code WebMvcContextConfiguration} and {@code WebflowContextConfiguration}. This Servlet will be registered using
 * JEE's programmatical API support.
 * <p/>
 * 
 * Note: the {@code OpenEntityManagerInViewFilter} is only enabled for pages served solely via Spring MVC. For pages
 * being served via WebFlow we configured WebFlow to use the JpaFlowExecutionListener.
 *
 * @author Marten Deinum
 *
 */
@SpringBootApplication
public class BookstoreApplication {

	public static void main(String... args) {
		SpringApplication.run(BookstoreApplication.class);
	}

}
