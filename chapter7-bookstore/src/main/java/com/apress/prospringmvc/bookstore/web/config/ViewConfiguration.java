package com.apress.prospringmvc.bookstore.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesView;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

/**
 * Spring MVC configuration for the View Technologies.
 * 
 * @author Marten Deinum

 * 
 */
@Configuration
public class ViewConfiguration {

    @Bean
    public TilesConfigurer tilesConfigurer() {
      final TilesConfigurer configurer = new TilesConfigurer();
      configurer.setDefinitions("WEB-INF/tiles.xml");
      configurer.setCheckRefresh(true);
      return configurer;
    }

    @Bean
    public TilesViewResolver tilesViewResolver() {
        final TilesViewResolver resolver = new TilesViewResolver();
        resolver.setViewClass(TilesView.class);
			  resolver.setOrder(2);
        return resolver;
    }

}
