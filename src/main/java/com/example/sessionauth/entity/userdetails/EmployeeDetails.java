package com.example.sessionauth.entity.userdetails;

import com.example.sessionauth.entity.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;

public record EmployeeDetails(Employee employee) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.employee.getAuthorities();
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
        return employee.isEnabled();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EmployeeDetails that)) return false;
        return Objects.equals(employee, that.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee);
    }
}
