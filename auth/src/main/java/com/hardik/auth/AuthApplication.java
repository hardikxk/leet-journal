package com.hardik.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class AuthApplication {

    private static final Logger log = LoggerFactory.getLogger(AuthApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Bean
    ApplicationListener<AuthenticationSuccessEvent> authSuccess() {
        return (auth) -> {
            var res = auth.getAuthentication();
            log.info("Logged in as: {} Type: {}", res.getName(), res.getClass().getSimpleName());
        };
    }

}
