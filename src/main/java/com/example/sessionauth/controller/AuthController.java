package com.example.sessionauth.controller;

import com.example.sessionauth.dto.EmployeeDTO;
import com.example.sessionauth.service.AuthService;
import com.example.sessionauth.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping(path = "/api/v1/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    private final EmployeeService employeeService;

    @Autowired
    public AuthController(AuthService authService, EmployeeService employeeService) {
        this.authService = authService;
        this.employeeService = employeeService;
    }

    /**
     * Public APIs needed to sign up employees
     *
     * @param employeeDTO
     * @return ResponseEntity
     * **/
    @PostMapping(path = "/signup")
    public ResponseEntity<?> signUpEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("Employee sign up called from {}", AuthController.class);
        employeeService.signupEmployee(employeeDTO);
        return ResponseEntity
                .status(CREATED)
                .body("CREATED");
    }

    /**
     * Public API that allows an employee to login
     *
     * @param employeeDTO
     * @param request
     * @param response
     * @return AuthResponse
     * **/
    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    public void loginEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        log.info("Employee logged in called from {}", AuthController.class);
        authService.loginEmployee(employeeDTO, request, response);
    }


    /**
     * Protected route only employees with the role ADMIN can hit
     *
     * @param authentication
     * @return String
     * **/
    @GetMapping(path = "/authenticated")
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public String getAuthenticated(Authentication authentication) {
        return "Admin name is " + authentication.getName();
    }

    /**
    * Protected route. Any authenticated employee can his this
    *
    * @param authentication
    * @return String
    * **/
    @GetMapping(path = "/employee")
    @ResponseStatus(HttpStatus.OK)
    public String onlyEmployeesCanHitThisRoute(Authentication authentication) {
        return "An Admin or Employee can hit this rout. Employees name is " + authentication.getName();
    }

}
