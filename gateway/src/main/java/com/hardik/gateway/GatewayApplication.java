package com.hardik.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.TokenRelayFilterFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Value;

@SpringBootApplication
public class GatewayApplication {

    static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    RouterFunction<ServerResponse> backendRoutes(@Value("${PROBLEMS_SERVICE_URI}") String problemsUri) {
        return route()
                .before(BeforeFilterFunctions.uri(problemsUri))
                .before(BeforeFilterFunctions.rewritePath("/problems/", "/"))
                .filter(TokenRelayFilterFunctions.tokenRelay())
                .GET("/problems/**", http())
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> mailRoute(@Value("${MAIL_SERVICE_URI}") String mailUri) {
        return route()
                .before(BeforeFilterFunctions.uri(mailUri))
                .before(BeforeFilterFunctions.rewritePath("/mail/", "/"))
                .filter(TokenRelayFilterFunctions.tokenRelay())
                .GET("/mail/**", http())
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> aiRoute(@Value("${AI_SERVICE_URI}") String aiUri) {
        return route()
                .before(BeforeFilterFunctions.uri(aiUri))
                .before(BeforeFilterFunctions.rewritePath("/ai/", "/"))
                .filter(TokenRelayFilterFunctions.tokenRelay())
                .GET("/ai/**", http())
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> executionRoute(@Value("${PROBLEMS_SERVICE_URI}") String problemsUri) {
        return route()
                .before(BeforeFilterFunctions.uri(problemsUri))
                .before(BeforeFilterFunctions.rewritePath("/code/", "/"))
                .filter(TokenRelayFilterFunctions.tokenRelay())
                .GET("/code/**", http())
                .build();
    }
}
