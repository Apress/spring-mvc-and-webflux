package com.apress.prospringmvc.bookstore;

import org.springframework.web.server.adapter.AbstractReactiveWebInitializer;

public class WebAppInitializer extends AbstractReactiveWebInitializer {

	@Override
	protected Class<?>[] getConfigClasses() {
		return new Class<?>[]{AppConfiguration.class};
	}

}
