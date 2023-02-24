package com.example.sessionauth.controller;

import com.example.sessionauth.dto.EmployeeDTO;
import com.example.sessionauth.service.AuthService;
import com.example.sessionauth.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(path = "/api/v1/auth")
public class AuthController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;
    private final EmployeeService employeeService;

    public AuthController(AuthService authService, EmployeeService employeeService) {
        this.authService = authService;
        this.employeeService = employeeService;
    }

    /*
     * Public APIs needed to sign up employees
     *
     * @param employeeDTO
     * @return ResponseEntity
     * */
    @PostMapping(path = "/signup")
    public ResponseEntity<?> signUpEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        LOGGER.info("Employee sign up called from {}", AuthController.class);
        employeeService.signupEmployee(employeeDTO);
        return ResponseEntity
                .status(CREATED)
                .body("CREATED");
    }

    /*
     * Public API that allows an employee to login
     *
     * @param employeeDTO
     * @return AuthResponse
     * */
    @PostMapping(path = "/login")
    public void loginEmployee(@Valid @RequestBody EmployeeDTO employeeDTO,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        LOGGER.info("Employee logged in called from {}", AuthController.class);
        authService.loginEmployee(employeeDTO, request, response);
    }

    /*
    *
    * */
    @GetMapping(path = "authenticated")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN')")
    public String getAuthenticated(Authentication authentication) {
        return "Authenticated " + authentication.getPrincipal();
    }



}
