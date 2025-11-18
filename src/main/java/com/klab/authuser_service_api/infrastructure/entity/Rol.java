package com.klab.authuser_service_api.infrastructure.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("seguridad.roles")
public class Rol {
    @Id
    private Integer idrol;
    private String nombre;
    private String descripcion;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String creador;
 }
