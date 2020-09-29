package com.apress.prospringmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

/**
 * Helper class to log some basic information about the {@link ApplicationContext}
 *
 * @author Marten Deinum
 */
public class ApplicationContextLogger {

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationContextLogger.class);

	public static void log(ApplicationContext context) {
		LOG.info("Context: {},{}", context.getClass(), context.getDisplayName());
		LOG.info("Beans: {}", context.getBeanDefinitionCount());
		LOG.info("Active profiles: {}", StringUtils.arrayToCommaDelimitedString(context.getEnvironment().getActiveProfiles()));
		for (var name : context.getBeanDefinitionNames()) {
			LOG.info("Bean: {}", name);
		}
	}
}
