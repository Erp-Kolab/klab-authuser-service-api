package com.klab.authuser_service_api.repository;


import com.klab.authuser_service_api.infrastructure.entity.Rol;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RolRepository extends ReactiveCrudRepository<Rol, Integer> {
}