package com.example.sessionauth.repository;

import com.example.sessionauth.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    @Query("""
        SELECT e FROM Employee e WHERE e.email = :email
    """)
    Optional<Employee> findEmployeeByEmail(@Param(value = "email") String email);
}
