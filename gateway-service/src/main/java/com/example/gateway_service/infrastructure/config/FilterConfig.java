package com.example.gateway_service.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.gateway_service.infrastructure.security.AuthorizationFilter;

@Configuration
public class FilterConfig {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Bean
    public FilterRegistrationBean<AuthorizationFilter> authorizationFilter() {
        FilterRegistrationBean<AuthorizationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new AuthorizationFilter(jwtSecret));
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}
