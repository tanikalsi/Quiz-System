package com.quizsystem.quiz_console_client;

public class AuthenticationResponse {
    private String jwt;

    // A no-arg constructor is needed for deserialization
    public AuthenticationResponse() {
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}