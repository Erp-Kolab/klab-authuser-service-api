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
@Table("seguridad.personal")
public class Personal {
    @Id
    private Integer idpersonal;
    private Integer idusu;
    private Integer idtipodoc;
    private String nombre;
    private String apellidopa;
    private String apellidoma;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String creador;
}