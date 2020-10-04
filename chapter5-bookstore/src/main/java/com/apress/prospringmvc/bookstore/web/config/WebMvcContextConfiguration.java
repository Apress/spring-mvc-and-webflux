package com.apress.prospringmvc.bookstore.web.config;

import com.apress.prospringmvc.bookstore.converter.StringToEntityConverter;
import com.apress.prospringmvc.bookstore.domain.Category;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.validation.Validator;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Extending the Spring MVC configuration
 *
 * @author Marten Deinum
 */
@Configuration
public class WebMvcContextConfiguration implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**/*")
				.addResourceLocations("classpath:/META-INF/resources/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/index.htm").setViewName("index");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
	}

	/**
	 * The {@link LocaleChangeInterceptor} allows for the locale to be changed. It provides a <code>paramName</code>
	 * property which sets the request parameter to check for changing the language, the default is <code>locale</code>.
	 *
	 * @return the {@link LocaleChangeInterceptor}
	 */
	@Bean
	public HandlerInterceptor localeChangeInterceptor() {
		var localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	/**
	 * The {@link LocaleResolver} implementation to use. Specifies where to store the current selected locale.
	 *
	 * @return the {@link LocaleResolver}
	 */
	@Bean
	public LocaleResolver localeResolver() {
		return new CookieLocaleResolver();
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(categoryConverter());
		registry.addFormatter(new DateFormatter("dd-MM-yyyy"));
	}

	@Bean
	public StringToEntityConverter categoryConverter() {
		return new StringToEntityConverter(Category.class);
	}

	@Bean
	public ConfigurableWebBindingInitializer configurableWebBindingInitializer(Validator mvcValidator,
																																						 FormattingConversionService conversionService) {
		var initializer = new ConfigurableWebBindingInitializer();
		initializer.setDirectFieldAccess(true);
		initializer.setValidator(mvcValidator);
		initializer.setConversionService(conversionService);
		return initializer;
	}

}
