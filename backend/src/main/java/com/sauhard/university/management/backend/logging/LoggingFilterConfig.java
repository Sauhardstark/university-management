package com.sauhard.university.management.backend.logging;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingFilterConfig {

	@Bean
	public FilterRegistrationBean<LoggingRequestContextFilter> loggingRequestContextFilter() {
		FilterRegistrationBean<LoggingRequestContextFilter> reg = new FilterRegistrationBean<>();
		reg.setFilter(new LoggingRequestContextFilter());
		reg.setOrder(1);
		return reg;
	}

}
