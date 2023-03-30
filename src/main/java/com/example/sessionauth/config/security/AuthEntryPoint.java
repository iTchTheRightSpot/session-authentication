package com.example.sessionauth.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * This call helps globally handle spring security exceptions using @ControllerAdvice
 * (I do not have a @ControllerAdvice implemented for this project). The controller advice is an interceptor that
 * allows us to use the same exception handling across the application
 * For better understanding visit below
 * <a href="https://www.baeldung.com/spring-security-exceptionhandler#:~:text=Spring%20security%20exceptions%20can%20be,a%20custom%20implementation%20of%20AuthenticationEntryPoint">...</a>.
 * */
@Component(value = "authEntryPoint")
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final HandlerExceptionResolver resolver;

    @Autowired
    public AuthEntryPoint(@Qualifier(value = "handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        this.resolver.resolveException(request, response, null, authException);
    }

}
