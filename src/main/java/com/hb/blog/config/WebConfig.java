package com.hb.blog.config;

import com.hb.blog.filter.SnakeToCamelCaseFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Bean
    public FilterRegistrationBean<SnakeToCamelCaseFilter> snakeToCamelCaseFilterRegistration() {
        FilterRegistrationBean<SnakeToCamelCaseFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new SnakeToCamelCaseFilter());
        registration.addUrlPatterns("/api/v1/*"); // set URL patterns to filter
        return registration;
    }
}
