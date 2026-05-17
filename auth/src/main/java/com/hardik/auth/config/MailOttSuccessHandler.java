package com.hardik.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class MailOttSuccessHandler implements OneTimeTokenGenerationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(MailOttSuccessHandler.class);

    private final JdbcTemplate jdbcTemplate;
    private final RestClient restClient;

    public MailOttSuccessHandler(JdbcTemplate jdbcTemplate, RestClient.Builder restClientBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.restClient = restClientBuilder.baseUrl("http://127.0.0.1:8002").build();
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken) throws IOException, ServletException {
        String username = oneTimeToken.getUsername();
        String tokenValue = oneTimeToken.getTokenValue();

        // Query email
        String email = null;
        try {
            email = jdbcTemplate.queryForObject("SELECT email FROM users WHERE username = ?", String.class, username);
        } catch (Exception e) {
            // Log or handle missing user
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User not found or missing email");
            return;
        }

        if (email == null || email.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User does not have an email configured");
            return;
        }

        // Send email
        String loginUrl = "http://127.0.0.1:8000/login/ott?token=" + tokenValue;
        // DEV: log the URL so you can test without the mail-service running
        log.warn("\n\n>>> OTT LOGIN URL (dev only): {}\n", loginUrl);
        String subject = "Your One-Time Login Token";
        String text = "Click the following link to log in: " + loginUrl;

        final String finalEmail = email;

        try {
            restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/mime")
                            .queryParam("to", finalEmail)
                            .queryParam("subject", subject)
                            .queryParam("text", text)
                            .build())
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to send email");
            return;
        }

        // Return a response to the user
        response.setContentType("text/html");
        response.getWriter().write("A one-time token has been sent to your email address.");
    }
}
