package com.example.sessionauth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.EAGER;

@Table(name = "EMPLOYEE") @Entity
@NoArgsConstructor @Getter @Setter
public class Employee implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeID;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "account_enable")
    private boolean enabled;

    @Column(name = "credentials_expired")
    private boolean credentialsNonExpired;

    @Column(name = "account_expired")
    private boolean accountNonExpired;

    @Column(name = "account_locked")
    private boolean locked;

    @JsonIgnore
    @OneToMany(cascade = {PERSIST, MERGE, REMOVE}, fetch = EAGER, mappedBy = "employee", orphanRemoval = true)
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        this.roles.add(role);
        role.setEmployee(this);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this
                .roles
                .stream() //
                .map(role -> new SimpleGrantedAuthority(role.getRoleEnum().toString()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return isEnabled() == employee.isEnabled()
                && isCredentialsNonExpired() == employee.isCredentialsNonExpired()
                && isAccountNonExpired() == employee.isAccountNonExpired()
                && isLocked() == employee.isLocked()
                && Objects.equals(getEmployeeID(), employee.getEmployeeID())
                && Objects.equals(getEmail(), employee.getEmail())
                && Objects.equals(getPassword(), employee.getPassword())
                && Objects.equals(getRoles(), employee.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getEmployeeID(),
                getEmail(),
                getPassword(),
                isEnabled(),
                isCredentialsNonExpired(),
                isAccountNonExpired(),
                isLocked(),
                getRoles()
        );
    }

}
