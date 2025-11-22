package com.klab.authuser_service_api.service;


import com.klab.authuser_service_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    @Autowired
    UserRepository userRepository;
    @Autowired
     EmailService emailService;

    private static final int CODE_LENGTH = 6;
    private static final int CODE_EXPIRATION_MINUTES = 2;
    private final Random random = new Random();

    public Mono<Boolean> sendVerificationCode(String email) {
        return userRepository.findByEmail(email)
                .flatMap(user -> {
                    String code = generateVerificationCode();
                    log.info("Generando código para {}: {}", email, code);

                    return userRepository.updateVerificationCode(email, code)
                            .then(Mono.defer(() -> {
                                log.info("✅ Código guardado en BD para: {}", email);
                                return emailService.sendVerificationCode(email, code);
                            }));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.warn("Usuario no encontrado con email: {}", email);
                    return Mono.just(false);
                }));
    }

    private String generateVerificationCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(random.nextInt(10)); // Números del 0-9
        }
        return code.toString();
    }
}