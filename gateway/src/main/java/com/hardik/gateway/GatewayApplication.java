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

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    RouterFunction<ServerResponse> backendRoutes(){
        return route ()
                .before(BeforeFilterFunctions.uri("http://localhost:8001/"))
                .before(BeforeFilterFunctions.rewritePath("/problems/", "/"))
                .filter(TokenRelayFilterFunctions.tokenRelay())
                .GET("/problems/**", http())
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> mailRoute(){
        return route ()
                .before(BeforeFilterFunctions.uri("http://localhost:8002/"))
                .before(BeforeFilterFunctions.rewritePath("/mail/", "/"))
                .filter(TokenRelayFilterFunctions.tokenRelay())
                .GET("/mail/**", http())
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> aiRoute(){
        return route ()
                .before(BeforeFilterFunctions.uri("http://localhost:8003/"))
                .before(BeforeFilterFunctions.rewritePath("/ai/", "/"))
                .filter(TokenRelayFilterFunctions.tokenRelay())
                .GET("/ai/**", http())
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> executionRoute(){
        return route ()
                .before(BeforeFilterFunctions.uri("http://localhost:8001/"))
                .before(BeforeFilterFunctions.rewritePath("/code/", "/"))
                .filter(TokenRelayFilterFunctions.tokenRelay())
                .GET("/code/**", http())
                .build();
    }

    // Frontend proxy moved to FrontendProxyController
}
