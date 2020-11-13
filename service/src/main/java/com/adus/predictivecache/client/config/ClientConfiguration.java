package com.adus.predictivecache.client.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public FilterRegistrationBean requestFilterRegistration(RequestFilter requestFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(requestFilter);
        registration.addUrlPatterns("/api/*");
        registration.setName("requestFilter");
        registration.setOrder(1);
        return registration;
    }
}
