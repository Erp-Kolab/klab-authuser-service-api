package com.klab.authuser_service_api.infrastructure.controller;


import com.klab.authuser_service_api.infrastructure.dto.request.AuthRequest;
import com.klab.authuser_service_api.infrastructure.dto.response.AuthResponse;
import com.klab.authuser_service_api.service.AuthService;
import com.klab.authuser_service_api.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    JwtService jwtService;

    @PostMapping("/login")
    @Operation(summary = "User login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest request) {
        return authService.authenticate(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().build()));
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate token")
    public Mono<ResponseEntity<Boolean>> validateToken(@RequestHeader("Autorization_validate") String authorizationHeader) {
        try {
            // Verificar que el header no sea nulo o vacío
            if (authorizationHeader == null || authorizationHeader.isBlank()) {
                return Mono.just(ResponseEntity.badRequest().body(false));
            }

            // Extraer el token (remover "Bearer " si existe)
            String jwtToken = authorizationHeader.startsWith("Bearer ")
                    ? authorizationHeader.substring(7)
                    : authorizationHeader;

            System.out.println("Token recibido para validación: " + jwtToken);

            return authService.validateToken(jwtToken)
                    .map(ResponseEntity::ok)
                    .onErrorReturn(ResponseEntity.ok(false));
        } catch (Exception e) {
            System.err.println("Error en validateToken: " + e.getMessage());
            return Mono.just(ResponseEntity.ok(false));
        }
    }

    @GetMapping("/test-token")
    @Operation(summary = "Generate test token")
    public Mono<ResponseEntity<Map<String, String>>> generateTestToken() {
        try {
            String testToken = jwtService.generateToken("admin");
            Map<String, String> response = new HashMap<>();
            response.put("token", testToken);
            response.put("message", "Use this token for testing");

            System.out.println("Token generado para testing: " + testToken);

            return Mono.just(ResponseEntity.ok(response));
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return Mono.just(ResponseEntity.status(500).body(error));
        }
    }
    @GetMapping("/validate-debug")
    @Operation(summary = "Validate token with detailed debug info")
    public Mono<ResponseEntity<Map<String, Object>>> validateTokenDebug(@RequestHeader("Authorization") String authorizationHeader) {

        Map<String, Object> debugInfo = new HashMap<>();
        debugInfo.put("timestamp", Instant.now().toString());
        debugInfo.put("authorization_header_received", authorizationHeader);

        try {
            // Verificar header
            if (authorizationHeader == null || authorizationHeader.isBlank()) {
                debugInfo.put("error", "Authorization header is null or empty");
                return Mono.just(ResponseEntity.badRequest().body(debugInfo));
            }

            // Extraer token
            String jwtToken = authorizationHeader.startsWith("Bearer ")
                    ? authorizationHeader.substring(7)
                    : authorizationHeader;

            debugInfo.put("jwt_token_extracted", jwtToken);
            debugInfo.put("jwt_token_length", jwtToken.length());

            // Validar formato básico
            if (jwtToken.split("\\.").length != 3) {
                debugInfo.put("error", "JWT must have 3 parts separated by dots");
                return Mono.just(ResponseEntity.badRequest().body(debugInfo));
            }

            // Intentar extraer información del token
            try {
                String username = jwtService.extractUsername(jwtToken);
                Date expiration = jwtService.extractExpiration(jwtToken);
                boolean isExpired = jwtService.isTokenExpired(jwtToken);

                debugInfo.put("username_from_token", username);
                debugInfo.put("expiration", expiration);
                debugInfo.put("is_expired", isExpired);
                debugInfo.put("current_time", new Date());

            } catch (Exception e) {
                debugInfo.put("token_parsing_error", e.getMessage());
            }

            // Validar con el servicio
            Boolean isValid = authService.validateToken(jwtToken).blockOptional().orElse(false);
            debugInfo.put("validation_result", isValid);

            return Mono.just(ResponseEntity.ok(debugInfo));

        } catch (Exception e) {
            debugInfo.put("exception", e.getMessage());
            debugInfo.put("exception_type", e.getClass().getName());
            return Mono.just(ResponseEntity.badRequest().body(debugInfo));
        }
    }
}