package com.klab.authuser_service_api.infrastructure.dto.request;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class VerificationCodeRequest {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

}
