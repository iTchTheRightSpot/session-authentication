package com.example.sessionauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.json.Json;
import javax.json.JsonObject;

public record AuthDTO(@JsonProperty("email") String email, @JsonProperty("password") String password) {

    // Needed for integration testing
    public JsonObject convertToJSON() {
        return Json.createObjectBuilder()
                .add("email", email())
                .add("password", password())
                .build();
    }

}
