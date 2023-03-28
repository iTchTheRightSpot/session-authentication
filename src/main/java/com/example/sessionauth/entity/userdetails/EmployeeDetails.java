package com.example.sessionauth.entity.userdetails;

import com.example.sessionauth.entity.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public record EmployeeDetails(Employee employee) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this
                .employee
                .getRoles() //
                .stream() //
                .map(role -> new SimpleGrantedAuthority(role.getRoleEnum().toString())) //
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
        return this.employee.getAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.employee.getLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.employee.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return employee.getEnabled();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UserDetails
                && this.employee.getEmail().equals(((UserDetails) obj).getUsername())
                && this.employee.getPassword().equals(((UserDetails) obj).getPassword())
                && this.employee.getEnabled().equals(((UserDetails) obj).isEnabled())
                && Objects.equals(this.employee.getLocked(), ((UserDetails) obj).isAccountNonLocked())
                && Objects.equals(this.employee.getAccountNonExpired(), ((UserDetails) obj).isAccountNonExpired())
                && Objects.equals(this.employee.getCredentialsNonExpired(), ((UserDetails) obj).isCredentialsNonExpired())
                && this.employee.getAuthorities().equals(((UserDetails) obj).getAuthorities());
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee.getEmail(), employee.getPassword(), employee.getEnabled(),
                employee.getLocked(), employee.getAccountNonExpired(), employee.getCredentialsNonExpired(),
                employee.getAuthorities());
    }

}
