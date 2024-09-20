package com.congdinh.vivustore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.congdinh.vivustore.interceptors.LoggingInterceptor;

@Configuration
public class WebMVCConfig implements WebMvcConfigurer {

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Add LoggingInterceptor to intercept all incoming requests
        registry.addInterceptor(loggingInterceptor).addPathPatterns("/**");
    }
}
