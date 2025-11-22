package com.klab.authuser_service_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
public class EmailService {



    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    public Mono<Boolean> sendVerificationCode(String email, String code) {
        return Mono.fromCallable(() -> {
            // En producción, aquí integrarías con un servicio de email real
            LOGGER.info("📧 Código de verificación enviado a: {}", email);
            LOGGER.info("🔑 Código: {} (Válido por {} minutos)", code);
            LOGGER.info("=== EN PRODUCCIÓN ESTO ENVIARÍA UN EMAIL REAL ===");

            // Simular envío exitoso
            return true;
        });
    }
}
