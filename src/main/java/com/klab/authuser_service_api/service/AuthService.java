package com.klab.authuser_service_api.service;

import com.klab.authuser_service_api.infrastructure.dto.request.AuthRequest;
import com.klab.authuser_service_api.infrastructure.dto.response.AuthResponse;
import com.klab.authuser_service_api.infrastructure.entity.User;
import com.klab.authuser_service_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private final JwtService jwtService;

    private static final int MAX_FAILED_ATTEMPTS = 3;

    public Mono<AuthResponse> authenticate(AuthRequest request) {
        return userRepository.findByUsername(request.getEmail())
                .flatMap(user -> validateCredentials(user, request.getPassword()))
                .switchIfEmpty(Mono.just(createErrorResponse("Usuario no encontrado")));
    }

    private Mono<AuthResponse> validateCredentials(User user, String rawPassword) {
        // Verificar si la cuenta está bloqueada
        if (user.getIsLocked() != null && user.getIsLocked() == 1) {
            return Mono.just(createErrorResponse("Cuenta bloqueada. Contacte al administrador"));
        }

        // DEBUG: Verificar qué está llegando
        System.out.println("Contraseña recibida: " + rawPassword);
        System.out.println("Hash en BD: " + user.getPasswordHash());

        // Verificar contraseña - USA LA INSTANCIA INYECTADA
        if (passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            LOGGER.info("✅ Contraseña VÁLIDA");

            // Login exitoso
            return handleSuccessfulLogin(user);
        } else {
            LOGGER.info("Contraseña INVALIDA");
            // Login fallido
            return handleFailedLogin(user);
        }
    }

    private Mono<AuthResponse> handleSuccessfulLogin(User user) {
        // Actualizar último login y resetear intentos fallidos
        return userRepository.updateSuccessfulLogin(user.getUserId())
                .then(Mono.fromCallable(() -> {
                    String token = jwtService.generateToken(user.getUsername());

                    return AuthResponse.builder()
                            .token(token)
                            .username(user.getUsername())
                            .message("Login exitoso")
                            .build();
                }));
    }

    private Mono<AuthResponse> handleFailedLogin(User user) {
        int newFailedAttempts = (user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts()) + 1;

        // Verificar si se debe bloquear la cuenta
        if (newFailedAttempts >= MAX_FAILED_ATTEMPTS) {
            return userRepository.updateFailedLoginAttempts(user.getUserId(), newFailedAttempts)
                    .then(Mono.just(createErrorResponse("Cuenta bloqueada por demasiados intentos fallidos")));
        }

        // Solo incrementar intentos fallidos
        return userRepository.updateFailedLoginAttempts(user.getUserId(), newFailedAttempts)
                .then(Mono.just(createErrorResponse("Credenciales inválidas. Intentos restantes: " + (MAX_FAILED_ATTEMPTS - newFailedAttempts))));
    }

    private AuthResponse createErrorResponse(String message) {
        return AuthResponse.builder()
                .token(null)
                .username(null)
                .message(message)
                .build();
    }
}