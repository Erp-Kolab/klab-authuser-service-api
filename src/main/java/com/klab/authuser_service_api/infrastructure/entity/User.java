package com.klab.authuser_service_api.infrastructure.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
@Data
@Table("security.users")
@Getter
@Setter
public class User {
    @Id
    private Integer userId;
    private String username;
    private String email;
    private String passwordHash;
    private Integer personalId;
    private LocalDateTime firstLoginDate;
    private LocalDateTime lastLoginDate;
    private Integer failedLoginAttempts;
    private Short isLocked;
    private Short isActive;
    private String code_valid;
    private Boolean status_code_valid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Integer createdBy;
    private Integer updatedBy;
}