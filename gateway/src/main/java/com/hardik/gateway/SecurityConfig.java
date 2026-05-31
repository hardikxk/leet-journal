package com.hardik.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Allow frontend paths to be accessed publicly
                .requestMatchers("/", "/login", "/_astro/**", "/favicon.ico", "/*.svg", "/*.png", "/*.jpg").permitAll()
                // Secure backend APIs
                .requestMatchers("/problems/**", "/mail/**", "/ai/**", "/code/**").authenticated()
                .anyRequest().permitAll()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
            );
        return http.build();
    }
}
