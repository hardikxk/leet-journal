package com.hardik.problemsservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI leetJournalOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Leet Journal Problems API")
                        .description("API documentation for problems-service")
                        .version("1.0"));
    }
}
