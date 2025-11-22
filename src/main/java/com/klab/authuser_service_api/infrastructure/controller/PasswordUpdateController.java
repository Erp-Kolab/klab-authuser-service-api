package com.klab.authuser_service_api.infrastructure.controller;

import com.klab.authuser_service_api.infrastructure.dto.request.UpdatePasswordRequest;
import com.klab.authuser_service_api.service.PasswordUpdateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Password Update", description = "APIs para actualización de contraseña con código de verificación")
public class PasswordUpdateController {

    private final PasswordUpdateService passwordUpdateService;

    @PostMapping("/update-password")
    @Operation(summary = "Actualizar contraseña con código",
            description = "Valida el código de verificación y actualiza la contraseña")
    public Mono<ResponseEntity<Map<String, String>>> updatePassword(
            @Valid @RequestBody UpdatePasswordRequest request) {

        return passwordUpdateService.updatePasswordWithCode(
                request.getEmail(),
                request.getCode(),
                request.getNewPassword()
        ).map(success -> {
            if (success) {
                return ResponseEntity.ok(Collections.singletonMap(
                        "message", "Contraseña actualizada exitosamente"
                ));
            } else {
                return ResponseEntity.badRequest().body(Collections.singletonMap(
                        "error", "Código inválido, expirado o usuario no encontrado"
                ));
            }
        });
    }



}