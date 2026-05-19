package com.hardik.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

@SpringBootApplication
public class AuthApplication {

    static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    ApplicationListener<AuthenticationSuccessEvent> authSuccess() {
        return (auth) -> {
            var res = auth.getAuthentication();
            IO.println("Logged in as: " + res.getName() + " Type: "  + res.getClass().getSimpleName());
        };
    }

}
