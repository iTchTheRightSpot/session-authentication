package com.example.sessionauth.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.FetchType.EAGER;

@Table(schema = "EMPLOYEE")
@Entity
@NoArgsConstructor
@Getter @Setter
public class Employee {
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
    private Boolean enabled = false; // For email validation after signing up

    @Column(name = "credentials_expired")
    private boolean credentialsNonExpired = true;

    @Column(name = "account_expired")
    private boolean accountNonExpired = true;

    @Column(name = "account_locked")
    private boolean locked = true; // For when an employee quits or is fired

    @JsonIgnore
    @OneToMany(cascade = {PERSIST, MERGE, REMOVE}, fetch = EAGER, mappedBy = "employee", orphanRemoval = true)
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        this.roles.add(role);
        role.setEmployee(this);
    }

}
