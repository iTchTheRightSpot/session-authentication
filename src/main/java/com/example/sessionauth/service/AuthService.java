package com.example.sessionauth.service;

import com.example.sessionauth.dto.EmployeeDTO;
import com.example.sessionauth.entity.Employee;
import com.example.sessionauth.entity.Role;
import com.example.sessionauth.enumeration.RoleEnum;
import com.example.sessionauth.repository.EmployeeRepo;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service @Slf4j
public class AuthService {

    @Value(value = "${custom.max.session}")
    private int maxSession;

    @Value(value = "${admin.email}")
    private String adminEmail;

    @Value("${employee.email}")
    private String test;

    private final EmployeeRepo employeeRepository;

    private final PasswordEncoder passwordEncoder;

    private final SecurityContextRepository securityContextRepository;

    private final SecurityContextHolderStrategy securityContextHolderStrategy;

    private final AuthenticationManager authManager;

    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    private final SessionRegistry sessionRegistry;

    @Autowired
    public AuthService(
            EmployeeRepo employeeRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authManager,
            FindByIndexNameSessionRepository<? extends Session> sessionRepository,
            SessionRegistry sessionRegistry
    ) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.sessionRepository = sessionRepository;
        this.sessionRegistry = sessionRegistry;
        this.securityContextRepository = new HttpSessionSecurityContextRepository();
        this.securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
    }


    /**
     * Method responsible for registering an employee
     *
     * @param dto
     * @throws IllegalStateException
     * @return String
     * **/
    public String register(EmployeeDTO dto) {
        String email = dto.getEmail().trim();
        String password = dto.getPassword().trim();

        Optional<Employee> exists = employeeRepository
                .findByPrincipal(email);

        if (exists.isPresent()) {
            throw new IllegalStateException(email + " already exists");
        }

        var employee = new Employee();
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(password));
        employee.setLocked(true); // true if not locked
        employee.setAccountNonExpired(true);
        employee.setCredentialsNonExpired(true);
        employee.setEnabled(false); // false for email validation
        employee.addRole(new Role(RoleEnum.EMPLOYEE));

        if (adminEmail.equals(email)) {
            employee.addRole(new Role(RoleEnum.ADMIN));
        }

        if (dto.getEmail().trim().equals(adminEmail) || dto.getEmail().trim().equals(test)) {
            employee.setEnabled(true);
        }

        log.info("New Employee saved");
        employeeRepository.save(employee);
        return "CREATED";
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
    private void validateMaxSession(Authentication authentication) {
        if (maxSession <= 0) {
            // If max session is negative means unlimited session
            return;
        }

        var principal = (UserDetails) authentication.getPrincipal();
        List<SessionInformation> sessions = this.sessionRegistry.getAllSessions(principal, false);

        if (sessions.size() >= maxSession) {
            sessions
                    .stream() //
                    .min(Comparator.comparing(SessionInformation::getLastRequest)) // Gets the oldest session
                    .ifPresent(sessionInfo -> {
                        String sessionID = sessionInfo.getSessionId();
                        this.sessionRepository.deleteById(sessionID);
                    });
        }
    }

}
