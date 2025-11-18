-- Migración inicial para datos básicos
INSERT INTO seguridad.tipodoc (nombre, created_at, is_active, creador)
VALUES
    ('DNI', NOW(), true, 'system'),
    ('Carnet de Extranjería', NOW(), true, 'system'),
    ('Pasaporte', NOW(), true, 'system')
ON CONFLICT DO NOTHING;

-- Insertar roles básicos
INSERT INTO seguridad.roles (nombre, descripcion, created_at, is_active, creador)
VALUES
    ('ADMIN', 'Administrador del sistema', NOW(), true, 'system'),
    ('USER', 'Usuario regular', NOW(), true, 'system'),
    ('GUEST', 'Usuario invitado', NOW(), true, 'system')
ON CONFLICT DO NOTHING;

-- Insertar permisos básicos
INSERT INTO seguridad.permisos (nombre, descripcion, created_at, is_active, creador)
VALUES
    ('READ', 'Permiso de lectura', NOW(), true, 'system'),
    ('WRITE', 'Permiso de escritura', NOW(), true, 'system'),
    ('DELETE', 'Permiso de eliminación', NOW(), true, 'system')
ON CONFLICT DO NOTHING;