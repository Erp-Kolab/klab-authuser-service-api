package com.klab.authuser_service_api.service;


import com.klab.authuser_service_api.infrastructure.entity.User;
import com.klab.authuser_service_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordUpdateService {

    private final  UserRepository userRepository;

    private final  BCryptPasswordEncoder passwordEncoder;
    private final Map<String, Boolean> updateInProgress = new ConcurrentHashMap<>();

    public Mono<Boolean> updatePasswordWithCode(String email, String code, String newPassword) {
        log.info("=== INICIANDO ACTUALIZACIÓN DE CONTRASEÑA ===");

        // SOLUCIÓN: Una sola suscripción forzada
        return Mono.fromCallable(() -> {
                    log.info("🎯 Iniciando operación única para: {}", email);
                    return email;
                })
                .flatMap(actualEmail -> userRepository.findByEmailWithCode(actualEmail))
                .flatMap(user -> {
                    // Guardar los datos ANTES de cualquier operación
                    final String userEmail = user.getEmail();
                    final String savedCode = user.getCode_valid();
                    final Boolean savedStatus = user.getStatus_code_valid();

                    log.info("🔍 Datos originales - Code: {}, Status: {}", savedCode, savedStatus);

                    // Validar con los datos guardados
                    if (savedStatus == null || !savedStatus || !savedCode.equals(code)) {
                        log.warn("❌ Validación fallida para: {}", userEmail);
                        return Mono.just(false);
                    }

                    String encodedPassword = passwordEncoder.encode(newPassword);
                    log.info("🔄 Actualizando para: {}", userEmail);

                    return userRepository.updatePasswordAndDisableCode(userEmail, encodedPassword)
                            .map(rows -> rows > 0);
                })
                .defaultIfEmpty(true)
                .doOnNext(result -> log.info("🎯 Resultado final: {}", result));
    }

    private Mono<Boolean> validateAndUpdatePassword(User user, String code, String newPassword) {
        // Hacer una copia de los datos ANTES de la validación
        final String userEmail = user.getEmail();
        final String dbCode = user.getCode_valid();
        final Boolean dbStatus = user.getStatus_code_valid();

        log.info("🔍 Validando con datos cacheados - Code: {}, Status: {}", dbCode, dbStatus);

        if (!isValidCode(user, code)) {
            log.warn("❌ Código inválido o expirado para: {}", userEmail);
            return Mono.just(false);
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        log.info("🔄 Actualizando contraseña para: {}", userEmail);

        return userRepository.updatePasswordAndDisableCode(userEmail, encodedPassword)
                .map(rowsUpdated -> {
                    log.info("📊 Filas actualizadas: {}", rowsUpdated);
                    boolean success = rowsUpdated > 0;
                    if (success) {
                        log.info("✅ Contraseña actualizada exitosamente para: {}", userEmail);
                    } else {
                        log.error("❌ No se pudo actualizar la contraseña para: {}", userEmail);
                    }
                    return success;
                })
                .onErrorReturn(false);
    }
    private boolean isValidCode(User user, String inputCode) {
        // Verificar que el código esté activo
        if (user.getStatus_code_valid() == null || !user.getStatus_code_valid()) {
            log.warn("Código no activo para: {}", user.getEmail());
            return false;
        }

        // Verificar que el código coincida
        if (user.getCode_valid() == null || !user.getCode_valid().equals(inputCode)) {
            log.warn("Código no coincide para: {} (esperado: {}, recibido: {})",
                    user.getEmail(), user.getCode_valid(), inputCode);
            return false;
        }

        log.info("✅ Código válido para: {}", user.getEmail());
        return true;
    }
}