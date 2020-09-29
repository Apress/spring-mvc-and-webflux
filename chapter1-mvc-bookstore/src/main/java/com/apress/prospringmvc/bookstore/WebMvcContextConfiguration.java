package com.apress.prospringmvc.bookstore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.apress.prospringmvc.bookstore.web.IndexController;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Spring MVC configuration
 * 
 * @author Marten Deinum
 * @author Koen Serneels
 * 
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.apress.prospringmvc.bookstore"})
public class WebMvcContextConfiguration implements WebMvcConfigurer {

	@Bean
	ViewResolver viewResolver(){
		var resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/");
		resolver.setSuffix(".jsp" );
		return resolver;
	}

}
