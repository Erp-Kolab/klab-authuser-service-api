package com.klab.authuser_service_api.service;

import com.klab.authuser_service_api.infrastructure.dto.request.AuthRequest;
import com.klab.authuser_service_api.infrastructure.dto.response.AuthResponse;
import com.klab.authuser_service_api.infrastructure.entity.Usuario;
import com.klab.authuser_service_api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    JwtService jwtService;
    @Autowired
    PasswordEncoder passwordEncoder;

//    public Mono<AuthResponse> authenticate(AuthRequest request) {
//        return usuarioRepository.findByUsername(request.getUsername())
//                .filter(usuario -> passwordEncoder.matches(request.getPassword(), request.getPassword())) // Aquí debes tener las contraseñas encriptadas
//                .map(usuario -> {
//                    String token = jwtService.generateToken(usuario.getNombre());
//                    return new AuthResponse(token, "Bearer", usuario.getNombre(), "Authentication successful");
//                })
//                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
//    }

    public Mono<AuthResponse> authenticate(AuthRequest request) {
        Mono<Usuario>  usu;
        usu = usuarioRepository.findByUsername(request.getUsername());
              return  usu.filter(usuario -> {
                    // Si el usuario no tiene password en BD, aceptar cualquier contraseña (temporal)
                    if (request.getPassword() == null) {
                        System.out.println(" Usuario sin password en BD, aceptando cualquier contraseña");
                        return true;
                    }

                    // Verificar contraseña con BCrypt
                    boolean passwordMatches = passwordEncoder.matches(request.getPassword(), usuario.getNombre());
                    System.out.println("Password verification: " + passwordMatches);
                    return passwordMatches;
                })
                .map(usuario -> {
                    String token = jwtService.generateToken(usuario.getNombre());
                    System.out.println(" Token generado para usuario: " + usuario.getNombre());
                    return new AuthResponse(token, "Bearer", usuario.getNombre(), "Authentication successful");
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
    }

    public Mono<Boolean> validateToken(String token) {
        try {
            System.out.println("Validando token: " + token);

            // Primero validar formato básico del token
            if (token == null || token.isBlank() || token.length() < 10) {
                return Mono.just(false);
            }

            // Extraer username del token
            String username = jwtService.extractUsername(token);
            System.out.println("Username extraído del token: " + username);

            if (username == null || username.isBlank()) {
                return Mono.just(false);
            }

            // Verificar que el usuario existe
            return usuarioRepository.findByUsername(username)
                    .map(usuario -> {
                        try {
                            boolean isValid = username.equals(usuario.getNombre()) &&
                                    !jwtService.isTokenExpired(token);
                            System.out.println("Token válido: " + isValid);
                            return isValid;
                        } catch (Exception e) {
                            System.err.println("Error validando token: " + e.getMessage());
                            return false;
                        }
                    })
                    .defaultIfEmpty(false);

        } catch (Exception e) {
            System.err.println("Excepción en validateToken: " + e.getMessage());
            return Mono.just(false);
        }
    }
}