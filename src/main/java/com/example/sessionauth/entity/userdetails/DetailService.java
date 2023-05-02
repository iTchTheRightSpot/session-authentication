package com.example.sessionauth.entity.userdetails;

import com.example.sessionauth.repository.EmployeeRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value = "detailService")
public class DetailService implements UserDetailsService {

    private final EmployeeRepo employeeRepo;

    public DetailService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String principal) throws UsernameNotFoundException {
        return this
                .employeeRepo
                .findByPrincipal(principal)
                .map(EmployeeDetails::new) //
                .orElseThrow(() -> new UsernameNotFoundException(principal + " not found"));
    }

}
