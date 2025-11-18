package com.klab.authuser_service_api.repository;


import com.klab.authuser_service_api.infrastructure.entity.Usuario;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, Integer> {

    @Query("SELECT * FROM seguridad.usuarios WHERE nombre = $1 AND is_active = true")
    Mono<Usuario> findByUsername(String username);

    Mono<Usuario> findByIdusuAndIsActiveTrue(Integer idusu);
}