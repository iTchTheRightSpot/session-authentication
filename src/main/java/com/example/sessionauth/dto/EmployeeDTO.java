package com.example.sessionauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class EmployeeDTO {
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
}
