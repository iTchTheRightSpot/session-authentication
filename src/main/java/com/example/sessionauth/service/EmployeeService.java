package com.example.sessionauth.service;

import com.example.sessionauth.dto.EmployeeDTO;
import com.example.sessionauth.entity.Employee;
import com.example.sessionauth.entity.Role;
import com.example.sessionauth.enumeration.RoleEnum;
import com.example.sessionauth.repository.EmployeeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
    @Value(value = "${admin.email}")
    private String adminEmail;

    private final EmployeeRepo employeeRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * Returns an employee object based on employee email
     *
     * @param employeeEmail
     * @throws IllegalStateException
     * @return Employee
     * **/
    public Employee findEmployeeByEmail(String employeeEmail) {
        return employeeRepository
                .findEmployeeByEmail(employeeEmail)
                .orElseThrow(() -> new IllegalStateException("Does not exist"));
    }

    /**
     * Method called when an employee signs up
     *
     * @param userDTO
     * @throws IllegalStateException
     * @return void
     * **/
    public void signupEmployee(EmployeeDTO userDTO) {
        String email = userDTO.getEmail().trim();
        String password = userDTO.getPassword().trim();

        Optional<Employee> checkIfUserEmailExist = employeeRepository
                .findEmployeeByEmail(email);
        if (checkIfUserEmailExist.isPresent()) {
            throw new IllegalStateException(email + " already exists");
        }

        var employee = new Employee();
        employee.setEmail(email);
        employee.setPassword(passwordEncoder.encode(password));
        employee.addRole(new Role(RoleEnum.EMPLOYEE));
        employee.setEnabled(true);

        if (adminEmail.equals(email)) {
            employee.addRole(new Role(RoleEnum.ADMIN));
        }

        log.info("New Employee saved");
        employeeRepository.save(employee);
    }

}
