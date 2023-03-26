package com.example.sessionauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.json.Json;
import javax.json.JsonObject;

@NoArgsConstructor
@Getter @Setter @ToString
public class EmployeeDTO {
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;

    // Needed for integration testing
    public JsonObject convertToJSON() {
        return Json.createObjectBuilder()
                .add("email", getEmail())
                .add("password", getPassword())
                .build();
    }

}
