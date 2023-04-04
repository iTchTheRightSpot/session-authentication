package com.example.sessionauth.service;

import com.example.sessionauth.dto.EmployeeDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @Slf4j
public class AuthService {

    @Value(value = "${custom.max.session}")
    private int maxSession;

    private final SecurityContextRepository securityContextRepository;

    private final SecurityContextHolderStrategy securityContextHolderStrategy;

    private final AuthenticationManager authManager;

    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    private final SessionRegistry sessionRegistry;

    @Autowired
    public AuthService(
            AuthenticationManager authManager,
            FindByIndexNameSessionRepository<? extends Session> sessionRepository,
            SessionRegistry sessionRegistry
    ) {
        this.authManager = authManager;
        this.sessionRepository = sessionRepository;
        this.sessionRegistry = sessionRegistry;
        this.securityContextRepository = new HttpSessionSecurityContextRepository();
        this.securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
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
        String password = dto.getPassword();

        // Validate User credentials
        var userNamePasswordToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authManager.authenticate(userNamePasswordToken);

        // Validate session constraint is not exceeded
        validateMaxSession(authentication);

        // Create a new context
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context, request, response);

        log.info("{} logged in", email);
    }

    /**
     * Method is responsible for validating user session is not exceeded. If it has been exceeded, the oldest valid
     * session is removed/ invalidated
     *
     * @param authentication
     * @return void
     * */
    public void validateMaxSession(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        List<SessionInformation> sessions = this.sessionRegistry.getAllSessions(email, false);

        if (sessions.size() >= maxSession) {
            String sessionID = sessions.get(0).getSessionId();
            Session session = this.sessionRepository.findById(sessionID);
            if (session != null) {
                this.sessionRepository.deleteById(sessionID);
            }
        }
    }

}
