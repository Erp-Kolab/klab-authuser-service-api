# 🚀 API de Autenticación

API para el manejo de autenticación y recuperación de contraseñas.  
Incluye endpoints para inicio de sesión, solicitud de código de recuperación y cambio de contraseña.

---

## 📌 Información General

**Versión:** 1.0.0  
# 📂 Endpoints

## 🔐 1. Iniciar Sesión  
**POST** `/auth/login`

Autentica al usuario mediante email y contraseña.

### 📥 Request Body
 
{
  "email": "usuario@ejemplo.com",
  "password": "miContraseña123"
}
📤 Responses
✅ 200 – Login exitoso
json
Copiar código
{
  "success": true,
  "message": "Login exitoso"
}
❌ 401 – Credenciales inválidas
json
Copiar código
{
  "success": false,
  "message": "Credenciales inválidas"
}
📧 2. Solicitar Recuperación de Contraseña
POST /auth/forgot-password

Envía un código de confirmación al correo del usuario para recuperar su contraseña.

📥 Request Body
json
Copiar código
{
  "email": "usuario@ejemplo.com"
}
📤 Responses
✅ 200 – Código enviado
json
Copiar código
{
  "success": true,
  "message": "Código de recuperación enviado al correo"
}
❌ 404 – Usuario no encontrado
json
Copiar código
{
  "success": false,
  "message": "Usuario no encontrado"
}
🔁 3. Restablecer Contraseña
POST /auth/reset-password

Cambia la contraseña utilizando el código enviado por correo.

📥 Request Body
json
Copiar código
{
  "email": "usuario@ejemplo.com",
  "code": "A1B2C3",
  "newPassword": "nuevaContraseña456"
}
📤 Responses
✅ 200 – Contraseña cambiada
json
Copiar código
{
  "success": true,
  "message": "Contraseña cambiada exitosamente"
}
❌ 400 – Código inválido o expirado
json
Copiar código
{
  "success": false,
  "message": "Código inválido o expirado"
}
🔒 Seguridad
La API utiliza Bearer Authentication (JWT).

Ejemplo de uso:
makefile
Copiar código
Authorization: Bearer <token>
🧱 Esquema de Error
json
Copiar código
{
  "success": false,
  "message": "Descripción del error",
  "error": "Detalle técnico"
}
📜 Licencia
Este proyecto forma parte del servicio de autenticación y su uso está sujeto a las políticas internas de la organización.

