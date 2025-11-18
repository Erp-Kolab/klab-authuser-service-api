package com.klab.authuser_service_api.infrastructure.dto.response;


import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private String message;

    public AuthResponse( ) {

    }

    public AuthResponse(String token, String type, String username, String message) {
        this.token = token;
        this.type = type;
        this.username = username;
        this.message = message;
    }
}