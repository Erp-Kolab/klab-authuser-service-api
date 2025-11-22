package com.klab.authuser_service_api.infrastructure.controller;


import com.klab.authuser_service_api.infrastructure.dto.request.AuthRequest;
import com.klab.authuser_service_api.infrastructure.dto.request.PeopleRequest;
import com.klab.authuser_service_api.infrastructure.dto.response.AuthResponse;
import com.klab.authuser_service_api.service.AuthService;
import com.klab.authuser_service_api.service.JwtService;
import com.klab.authuser_service_api.service.PeopleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    JwtService jwtService;
    @Autowired
    PeopleService peopleService;






    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario", description = "Valida las credenciales y retorna un JWT token")
    public Mono<ResponseEntity<AuthResponse>> login(@Valid @RequestBody AuthRequest authRequest) {
        return authService.authenticate(authRequest)
               .map(response -> {
                    if (response.getToken() != null) {
                         return ResponseEntity.ok(response);
                    } else {
                         return ResponseEntity.badRequest().body(response);
                       }
              });
        }

    @PostMapping("/getpeople")
    @Operation(summary = "get people")
    public Mono<ResponseEntity<Object>> getPeople(@RequestBody PeopleRequest request) {
        return peopleService.getPeopleByEmail(request)
                    .map(peopleResponse -> ResponseEntity.ok().body(peopleResponse));
    }


}