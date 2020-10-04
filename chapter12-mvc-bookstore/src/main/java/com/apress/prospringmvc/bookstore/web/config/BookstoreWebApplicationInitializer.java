package com.apress.prospringmvc.bookstore.web.config;

import com.apress.prospringmvc.bookstore.config.DbConfiguration;
import com.apress.prospringmvc.bookstore.config.JpaConfiguration;
import com.apress.prospringmvc.bookstore.web.config.sec.SecurityConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.h2.server.web.WebServlet;

/**
 * {@code WebApplicationInitializer} that will be called by Spring's {@code SpringServletContainerInitializer} as part
 * of the JEE {@code ServletContainerInitializer} pattern. This class will be called on application startup and will
 * configure our JEE and Spring configuration.
 * <p/>
 * 
 * It will first initializes our {@code AnnotationConfigWebApplicationContext} with the common {@link Configuration}
 * classes: {@code InfrastructureContextConfiguration} and {@code TestDataContextConfiguration} using a typical JEE
 * {@code ContextLoaderListener}.
 * <p/>
 * 
 * Next it creates a {@link DispatcherServlet}, being a normal JEE Servlet which will create on its turn a child
 * {@code AnnotationConfigWebApplicationContext} configured with the Spring MVC {@code Configuration} classes
 * {@code WebMvcContextConfiguration} and {@code WebflowContextConfiguration}. This Servlet will be registered using
 * JEE's programmatical API support.
 * <p/>
 * 
 * Note: the {@code OpenEntityManagerInViewFilter} is only enabled for pages served soley via Spring MVC. For pages
 * being served via WebFlow we configured WebFlow to use the JpaFlowExecutionListener.
 * 
 * @author Marten Deinum
 *
 */
public class BookstoreWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{DbConfiguration.class, JpaConfiguration.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{
				JpaConfiguration.class, // just because I was lazy and used repositories in the web tier
				WebMvcContextConfiguration.class,
				SecurityConfiguration.class};
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{"/"};
	}

	@Override
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter cef = new CharacterEncodingFilter();
		cef.setEncoding("UTF-8");
		cef.setForceEncoding(true);
		return new Filter[]{new HiddenHttpMethodFilter(), cef};
	}

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		ServletRegistration.Dynamic servlet = servletContext
				.addServlet("h2-console", new WebServlet());
		servlet.setLoadOnStartup(2);
		servlet.addMapping("/h2-console/*");
	}

}
