package com.example.gateway_service.infrastructure.http;

import com.example.gateway_service.application.ports.HttpForwarder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestTemplateHttpForwarder implements HttpForwarder {

    private final RestTemplate restTemplate;

    private static final Set<String> HOP_BY_HOP_HEADERS = Set.of(
        "connection",
        "keep-alive",
        "proxy-authenticate",
        "proxy-authorization",
        "te",
        "trailers",
        "transfer-encoding",
        "upgrade",
        "content-length"
    );

    @Override
    public ResponseEntity<String> forward(
        String targetUrl,
        HttpMethod method,
        String body,
        Map<String, String> headers
    ) {
        log.info("Forwarding {} request to {}", method, targetUrl);
        log.debug("Request body: {}", body);

        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach((key, value) -> {
            if (!HOP_BY_HOP_HEADERS.contains(key.toLowerCase())) {
                httpHeaders.add(key, value);
            }
        });

        if (body != null && !body.isEmpty()) {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        }

        HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                targetUrl,
                method,
                entity,
                String.class
            );

            log.info("Received response from {} with status {}", targetUrl, response.getStatusCode());
            log.debug("Response body: {}", response.getBody());

            HttpHeaders responseHeaders = new HttpHeaders();
            response.getHeaders().forEach((key, values) -> {
                if (!HOP_BY_HOP_HEADERS.contains(key.toLowerCase())) {
                    responseHeaders.addAll(key, values);
                }
            });

            return ResponseEntity
                .status(response.getStatusCode())
                .headers(responseHeaders)
                .body(response.getBody());

        } catch (HttpStatusCodeException e) {
            log.warn("Service returned error: {} {} - {}", e.getStatusCode(), e.getStatusText(), e.getResponseBodyAsString());

            HttpHeaders errorHeaders = new HttpHeaders();
            errorHeaders.setContentType(MediaType.APPLICATION_JSON);

            return ResponseEntity
                .status(e.getStatusCode())
                .headers(errorHeaders)
                .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Error forwarding request to {}: {}", targetUrl, e.getMessage(), e);

            HttpHeaders errorHeaders = new HttpHeaders();
            errorHeaders.setContentType(MediaType.APPLICATION_JSON);

            String errorBody = String.format("{\"message\":\"Service unavailable: %s\"}",
                e.getMessage() != null ? e.getMessage().replace("\"", "\\\"") : "Unknown error");

            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .headers(errorHeaders)
                .body(errorBody);
        }
    }
}
