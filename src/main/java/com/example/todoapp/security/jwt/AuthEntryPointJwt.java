package com.example.todoapp.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import org.springframework.web.servlet.HandlerExceptionResolver;

@Component("authEntryPointJwt")
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private final static Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        resolver.resolveException(request,response,null,authException);
    }
}
