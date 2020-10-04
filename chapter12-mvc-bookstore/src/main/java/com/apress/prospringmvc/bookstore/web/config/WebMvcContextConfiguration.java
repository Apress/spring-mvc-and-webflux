package com.apress.prospringmvc.bookstore.web.config;

import com.apress.prospringmvc.bookstore.converter.StringToEntityConverter;
import com.apress.prospringmvc.bookstore.domain.Cart;
import com.apress.prospringmvc.bookstore.domain.Category;
import com.apress.prospringmvc.bookstore.formatter.DateFormatAnnotationFormatterFactory;
import com.apress.prospringmvc.bookstore.web.interceptor.CommonDataInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.context.request.WebRequestInterceptor;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Properties;

/**
 * Configures Spring MVC.
 *
 * @author Marten Deinum
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.apress.prospringmvc.bookstore.web"})
public class WebMvcContextConfiguration implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**/*").addResourceLocations("classpath:/META-INF/resources/");
	}

	@Override
	public void addViewControllers(final ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
		registry.addRedirectViewController("/", "/index.htm");
		//commented this to populate this page with all bean names
		//registry.addViewController("/index.htm").setViewName("index");
	}

	@Override
	public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		registry.addWebRequestInterceptor(commonDataInterceptor());
	}

	@Bean
	public WebRequestInterceptor commonDataInterceptor() {
		return new CommonDataInterceptor();
	}

	//-- Start Locale Support (I18N) --//

	/**
	 * The {@link LocaleChangeInterceptor} allows for the locale to be changed. It provides a <code>paramName</code> property which sets
	 * the request parameter to check for changing the language, the default is <code>locale</code>.
	 *
	 * @return the {@link LocaleChangeInterceptor}
	 */
	@Bean
	public HandlerInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
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

	/**
	 * To resolve message codes to actual messages we need a {@link MessageSource} implementation. The default
	 * implementations use a {@link java.util.ResourceBundle} to parse the property files with the messages in it.
	 *
	 * @return the {@link MessageSource}
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/messages");
		messageSource.setUseCodeAsDefaultMessage(true);
		return messageSource;
	}

	//-- End Locale Support (I18N) --//

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(categoryConverter());
		registry.addFormatterForFieldAnnotation(new DateFormatAnnotationFormatterFactory());
	}

	@Bean
	public StringToEntityConverter categoryConverter() {
		return new StringToEntityConverter(Category.class);
	}

	@Bean()
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public Cart cart() {
		return new Cart();
	}

	@Override
	public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
		exceptionResolvers.add(simpleMappingExceptionResolver());
	}

	@Bean
	public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
		SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
		Properties mappings = new Properties();
		mappings.setProperty("AccessDeniedException", "login");
		//mappings.setProperty("BadCredentialsException", "login");
		mappings.setProperty("MaxUploadSizeExceededException", "error");

		Properties statusCodes = new Properties();
		mappings.setProperty("login", String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
		mappings.setProperty("error", String.valueOf(HttpServletResponse.SC_LENGTH_REQUIRED));

		exceptionResolver.setExceptionMappings(mappings);
		exceptionResolver.setStatusCodes(statusCodes);
		return exceptionResolver;
	}

	@Bean
	public MultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(1000000);
		return multipartResolver;
	}
}
