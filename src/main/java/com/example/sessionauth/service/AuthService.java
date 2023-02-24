package com.example.sessionauth.service;

import com.example.sessionauth.dto.EmployeeDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
public class AuthService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();
    @Value(value = "${custom.max-expiry.time}")
    private Long expiryTime;
    private final AuthenticationManager authManager;
    private final FindByIndexNameSessionRepository sessionRepository;

    public AuthService(AuthenticationManager authManager, FindByIndexNameSessionRepository sessionRepository) {
        this.authManager = authManager;
        this.sessionRepository = sessionRepository;
    }

    public void loginEmployee(EmployeeDTO dto,
                              HttpServletRequest request,
                              HttpServletResponse response) {
        LOGGER.info("Login service called {}", AuthService.class);
        String email = dto.getEmail().trim();
        String password = dto.getPassword().trim();

        // Validate User credentials
        var userNamePasswordToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authManager.authenticate(userNamePasswordToken);
        LOGGER.info("Authentication " + authentication);

        // Create a new context
        var newContext = SecurityContextHolder.createEmptyContext();
        newContext.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(newContext);
        this.securityContextRepository.saveContext(newContext, request, response);

        // Build Session
        Session session = sessionRepository.createSession();
        session.setMaxInactiveInterval(Duration.of(expiryTime, ChronoUnit.MINUTES));
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, newContext);
        this.sessionRepository.save(session);

        // Build Response Cookie
        ResponseCookie responseCookie = ResponseCookie
                .from("JSESSIONID", session.getId())
                .domain("localhost")
                .httpOnly(true)
                .secure(false)
                .maxAge(expiryTime)
                .path("/")
                .sameSite("Lax")
                .build();

        // Header
//        HttpHeaders headers = new HttpHeaders();
//        headers.setDate(Instant.now().atZone(ZoneOffset.UTC));
//        headers.set(SET_COOKIE, responseCookie.toString());

//        Cookie cookie = new Cookie("SESSION", );
//        cookie.setHttpOnly(true);
//        cookie.setSecure(false);
//        cookie.setMaxAge(expiryTime);
//        cookie.setDomain("localhost");
//        cookie.setPath("/");
//
//        response.addCookie(cookie);
    }


}
