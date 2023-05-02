package com.example.sessionauth.entity;

import com.example.sessionauth.enumeration.RoleEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Table(name = "ROLE")
@Entity
@NoArgsConstructor
@Getter @Setter
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "role_id", updatable = false, nullable = false)
    private Long roleID;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private RoleEnum roleEnum;

    @ManyToOne
    @JoinColumn(
            name = "employee_id",
            nullable = false,
            referencedColumnName = "employee_id",
            foreignKey = @ForeignKey(name = "role_employee_fk")
    )
    private Employee employee;

    public Role(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
    }

}
