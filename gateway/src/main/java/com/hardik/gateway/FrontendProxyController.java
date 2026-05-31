package com.hardik.gateway;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Enumeration;

@RestController
public class FrontendProxyController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String targetUrl = "http://127.0.0.1:4321";

    @RequestMapping(value = "/**")
    public void proxy(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = request.getRequestURI();
        if (path.startsWith("/problems") || path.startsWith("/mail") || path.startsWith("/ai") || path.startsWith("/code") || path.startsWith("/login") || path.startsWith("/oauth2")) {
            // These should be handled by Gateway routes, but just in case they reach here
            response.sendError(404);
            return;
        }

        String queryString = request.getQueryString();
        String url = targetUrl + path + (queryString != null ? "?" + queryString : "");

        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!headerName.equalsIgnoreCase("host")) {
                headers.addAll(headerName, Collections.list(request.getHeaders(headerName)));
            }
        }

        try {
            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(
                    new URI(url),
                    HttpMethod.valueOf(request.getMethod()),
                    new HttpEntity<>(headers),
                    byte[].class
            );

            response.setStatus(responseEntity.getStatusCode().value());
            responseEntity.getHeaders().forEach((key, values) -> {
                if (!key.equalsIgnoreCase("Transfer-Encoding")) {
                    values.forEach(value -> response.addHeader(key, value));
                }
            });

            if (responseEntity.getBody() != null) {
                response.getOutputStream().write(responseEntity.getBody());
            }

        } catch (HttpStatusCodeException e) {
            response.setStatus(e.getStatusCode().value());
            e.getResponseHeaders().forEach((key, values) -> {
                if (!key.equalsIgnoreCase("Transfer-Encoding")) {
                    values.forEach(value -> response.addHeader(key, value));
                }
            });
            response.getOutputStream().write(e.getResponseBodyAsByteArray());
        } catch (Exception e) {
            response.sendError(500, "Frontend proxy error: " + e.getMessage());
        }
    }
}
