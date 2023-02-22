package com.example.sessionauth.service;

import com.example.sessionauth.dto.EmployeeDTO;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    @Value(value = "${custom.max-expiry.time}")
    private Integer expiryTime;
    private final AuthenticationManager authManager;
    private final FindByIndexNameSessionRepository sessionRepository;

    public ResponseEntity<?> loginEmployee(EmployeeDTO dto) {
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
        SecurityContextHolder.setContext(newContext);

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
        HttpHeaders headers = new HttpHeaders();
        headers.setDate(Instant.now().atZone(ZoneOffset.UTC));
        headers.set(SET_COOKIE, responseCookie.toString());

        return ResponseEntity
                .ok()
                .headers(headers)
                .build();
    }


}
