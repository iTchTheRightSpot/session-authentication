package com.example.sessionauth.entity.userdetails;

import com.example.sessionauth.entity.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public record EmployeeDetails(Employee employee) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this
                .employee
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleEnum().toString()))
                .collect(Collectors.toSet());

    }

    @Override
    public String getPassword() {
        return this.employee.getPassword();
    }

    @Override
    public String getUsername() {
        return this.employee.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.employee.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.employee.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.employee.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return employee.getEnabled();
    }

}
