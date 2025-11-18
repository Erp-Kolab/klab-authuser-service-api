package com.klab.authuser_service_api.infrastructure.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Integer idusu;
    private String nombre;
    private LocalDateTime createdAt;
    private Boolean isActive;
}