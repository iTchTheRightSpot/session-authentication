package com.example.sessionauth.service;

import com.example.sessionauth.dto.EmployeeDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.json.Json;
import java.io.IOException;
import java.io.PrintWriter;

@Service
@Slf4j
public class AuthService {
    private final SecurityContextRepository securityContextRepository =
            new HttpSessionSecurityContextRepository();
    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();
    private final AuthenticationManager authManager;

    public AuthService(AuthenticationManager authManager) {
        this.authManager = authManager;
    }

    /**
     * After an employee is authenticated via the auth manager, I am manually storing the authentication
     * For a better understanding, click the link below
     * <a href="https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html">...</a>
     *
     * @param dto
     * @param request
     * @param response
     * @return void
     * **/
    public void loginEmployee(
            EmployeeDTO dto,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.info("Login service called {}", AuthService.class);
        String email = dto.getEmail().trim();
        String password = dto.getPassword().trim();

        // Validate User credentials
        var userNamePasswordToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authManager.authenticate(userNamePasswordToken);
        log.info("Authentication " + authentication);

        // Set response body
        try {
            response.getWriter().write("Successfully signed in");
            response.getWriter().flush();
        } catch (IOException e) {
            log.error("Failed to parse response after successfully signing in {}", AuthService.class);
        }

        // Create a new context
        SecurityContext newContext = SecurityContextHolder.createEmptyContext();
        newContext.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(newContext);
        this.securityContextRepository.saveContext(newContext, request, response);
    }


}
