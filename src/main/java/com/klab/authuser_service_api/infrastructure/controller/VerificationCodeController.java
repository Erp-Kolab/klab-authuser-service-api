package com.klab.authuser_service_api.infrastructure.controller;


import com.klab.authuser_service_api.infrastructure.dto.request.VerificationCodeRequest;
import com.klab.authuser_service_api.service.VerificationCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/verify")
@RequiredArgsConstructor
@Tag(name = "Verification Code", description = "APIs para envío de códigos de verificación")
public class VerificationCodeController {
    @Autowired
    VerificationCodeService verificationCodeService;

    @PostMapping("/send-verification-code")
    @Operation(summary = "Enviar código de verificación",
            description = "Envía un código de verificación al email del usuario")
    public Mono<ResponseEntity<Map<String, String>>> sendVerificationCode(
            @Valid @RequestBody VerificationCodeRequest request) {

        return verificationCodeService.sendVerificationCode(request.getEmail())
                .map(success -> {
                    if (success) {
                        return ResponseEntity.ok(Collections.singletonMap(
                                "message", "Código de verificación enviado a tu email"
                        ));
                    } else {
                        return ResponseEntity.badRequest().body(Collections.singletonMap(
                                "error", "Error al enviar el código de verificación o usuario no encontrado"
                        ));
                    }
                });
    }
}