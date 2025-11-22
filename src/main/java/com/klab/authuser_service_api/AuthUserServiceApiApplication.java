package com.klab.authuser_service_api;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.klab")
public class AuthUserServiceApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthUserServiceApiApplication.class, args);
    }
}
