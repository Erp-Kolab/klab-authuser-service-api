package com.klab.authuser_service_api.configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                    .info(new Info()
                        .title("KLab Auth User Service API")
                        .version("1.0.0")
                        .description("Microservicio de autenticación y gestión de usuarios")
                        .contact(new Contact()
                                .name("KLab Team")
                                .email("support@klab.com")));
    }
}