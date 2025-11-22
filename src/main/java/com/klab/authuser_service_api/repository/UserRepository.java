package com.klab.authuser_service_api.repository;

import com.klab.authuser_service_api.infrastructure.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Integer> {

    @Query("SELECT * FROM security.users WHERE email = :email AND is_active = 1")
    Mono<User>  findByEmail(String email);

    @Query("SELECT * FROM security.users WHERE email = :email AND is_active = 1")
    Mono<User> findByUsername(String email);

    @Query("UPDATE security.users SET failed_login_attempts = :attempts WHERE user_id = :userId")
    Mono<Integer> updateFailedLoginAttempts(Integer userId, Integer attempts);

    @Query("UPDATE security.users SET last_login_date = CURRENT_TIMESTAMP, failed_login_attempts = 0 WHERE user_id = :userId")
    Mono<Integer> updateSuccessfulLogin(Integer userId);

    @Query("UPDATE security.users SET code_valid = :code, status_code_valid = true, " +
            "updated_at = CURRENT_TIMESTAMP WHERE email = :email")
    Mono<Integer> updateVerificationCode(String email, String code);

    // Buscar usuario por email incluyendo información del código
    @Query("SELECT * FROM security.users WHERE email = :email AND is_active = 1")
    Mono<User> findByEmailWithCode(String email);

    @Query("UPDATE security.users SET " +
            "password_hash = :passwordHash, " +
            "status_code_valid = false, " +  // ⬅️ Agrega esto también
            "updated_at = CURRENT_TIMESTAMP " +
            "WHERE email = :email")
    Mono<Integer> updatePasswordAndDisableCode(String email, String passwordHash);
}