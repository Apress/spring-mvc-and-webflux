package com.apress.prospringmvc.bookstore.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

/**
 * Spring MVC configuration for the View Technologies.
 * References: http://docs.spring.io/spring/docs/4.0.6.RELEASE/spring-framework-reference/html/view.html#view-tiles-integrate
 *
 * @author Marten Deinum
 */
@Configuration
public class ViewConfiguration {

	/**
	 * Initialise Tiles on application startup and identify the location of the tiles configuration file, tiles.xml.
	 *
	 * @return tiles configurer
	 */
	@Bean
	public TilesConfigurer tilesConfigurer() {
		final TilesConfigurer configurer = new TilesConfigurer();
		configurer.setDefinitions("WEB-INF/tiles.xml");
		configurer.setCheckRefresh(true);
		return configurer;
	}

	/**
	 * Introduce a Tiles view resolver, this is a convenience implementation that extends URLBasedViewResolver.
	 *
	 * @return tiles view resolver
	 */
	@Bean
	public TilesViewResolver tilesViewResolver() {
		final TilesViewResolver resolver = new TilesViewResolver();
		resolver.setOrder(2);
		resolver.setViewClass(TilesView.class);
		return resolver;
	}

}

