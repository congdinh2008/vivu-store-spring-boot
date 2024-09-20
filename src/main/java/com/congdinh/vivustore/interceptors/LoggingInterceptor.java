package com.congdinh.vivustore.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    // PreHandle: This method is called before the request is processed
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        logger.info("Incoming Request: URL = {} , Method = {} , IP = {}",
                request.getRequestURL(), request.getMethod(), request.getRemoteAddr());
        return true;
    }

    // PostHandle: This method is called after the request is processed, before the
    // response is sent
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        logger.info("Completed Request: Status = {}", response.getStatus());
    }

    // AfterCompletion: This method is called after the complete request has
    // finished and response is sent
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (ex != null) {
            logger.error("Request Error: URL = {}, Error = {}", request.getRequestURL(), ex.getMessage());
        }
        logger.info("Request Finished: URL = {} , Status = {}", request.getRequestURL(), response.getStatus());
    }
}
