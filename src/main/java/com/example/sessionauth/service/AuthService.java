package com.example.sessionauth.service;

import com.example.sessionauth.dto.AuthDTO;
import com.example.sessionauth.entity.Employee;
import com.example.sessionauth.entity.Role;
import com.example.sessionauth.enumeration.RoleEnum;
import com.example.sessionauth.repository.EmployeeRepo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service @Setter
public class AuthService {

    @Value(value = "${custom.max.session}")
    private int maxSession;

    @Value(value = "${admin.email}")
    private String adminEmail;

    private final EmployeeRepo employeeRepository;

    private final PasswordEncoder passwordEncoder;

    private final SecurityContextRepository securityContextRepository;

    private final SecurityContextHolderStrategy securityContextHolderStrategy;

    private final AuthenticationManager authManager;

    private final RedisIndexedSessionRepository redisIndexedSessionRepository;

    private final SessionRegistry sessionRegistry;

    public AuthService(
            EmployeeRepo employeeRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authManager,
            RedisIndexedSessionRepository redisIndexedSessionRepository,
            SessionRegistry sessionRegistry,
            SecurityContextRepository securityContextRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.redisIndexedSessionRepository = redisIndexedSessionRepository;
        this.sessionRegistry = sessionRegistry;
        this.securityContextRepository = securityContextRepository;
        this.securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    }

    /**
     * Method responsible for registering an employee
     *
     * @param dto is an object that contains user credentials
     * @throws IllegalStateException is thrown when a user email does not exist
     * @return String
     * **/
    public String register(AuthDTO dto) {
        String email = dto.email().trim();

        Optional<Employee> exists = employeeRepository
                .findByPrincipal(email);

        if (exists.isPresent()) {
            throw new IllegalStateException(email + " exists");
        }

        var employee = new Employee();
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(dto.password()));
        employee.setLocked(true);
        employee.setAccountNonExpired(true);
        employee.setCredentialsNonExpired(true);
        employee.setEnabled(true);
        employee.addRole(new Role(RoleEnum.EMPLOYEE));

        if (adminEmail.equals(email)) {
            employee.addRole(new Role(RoleEnum.ADMIN));
        }

        employeeRepository.save(employee);
        return "Register!";
    }

    /**
     * After an employee is authenticated via the auth manager, I am manually storing the authentication
     * For a better understanding, click the link below
     * <a href="https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html">...</a>
     *
     * @param dto is a record. It accepts email and password
     * @param request of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @return String
     * **/
    public String login(AuthDTO dto, HttpServletRequest request, HttpServletResponse response) {
        // Validate User credentials
        Authentication authentication = authManager.authenticate(UsernamePasswordAuthenticationToken.unauthenticated(
                dto.email().trim(), dto.password()));

        // Validate session constraint is not exceeded
        validateMaxSession(authentication);

        // Create a new context
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        // Update SecurityContextHolder and Strategy
        this.securityContextHolderStrategy.setContext(context);
        this.securityContextRepository.saveContext(context, request, response);

        return "Logged In!";
    }

    /**
     * Method is responsible for validating user session is not exceeded. If it has been exceeded, the oldest valid
     * session is removed/ invalidated
     *
     * @param authentication of type Spring Core Authentication
     * */
    private void validateMaxSession(Authentication authentication) {
        // If max session is negative means unlimited session
        if (maxSession <= 0) {
            return;
        }

        var principal = (UserDetails) authentication.getPrincipal();
        List<SessionInformation> sessions = this.sessionRegistry.getAllSessions(principal, false);

        if (sessions.size() >= maxSession) {
            sessions.stream() //
                    // Gets the oldest session
                    .min(Comparator.comparing(SessionInformation::getLastRequest)) //
                    .ifPresent(sessionInfo -> this.redisIndexedSessionRepository.deleteById(sessionInfo.getSessionId()));
        }
    }

}
