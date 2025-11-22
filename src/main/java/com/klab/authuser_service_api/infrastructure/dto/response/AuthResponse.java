package com.klab.authuser_service_api.infrastructure.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class AuthResponse {
    private String token;
    private String type;
    private String username;
    private String message;

    public AuthResponse() {
        this.type = "Bearer";
    }

    public AuthResponse(String token, String type, String username, String message) {
        this.token = token;
        this.type = type != null ? type : "Bearer";
        this.username = username;
        this.message = message;
    }
}
