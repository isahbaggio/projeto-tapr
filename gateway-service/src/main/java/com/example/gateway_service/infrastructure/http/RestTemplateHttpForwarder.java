package com.example.gateway_service.infrastructure.http;

import com.example.gateway_service.application.ports.HttpForwarder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class RestTemplateHttpForwarder implements HttpForwarder {

    private final RestTemplate restTemplate;

    @Override
    public ResponseEntity<String> forward(
        String targetUrl,
        HttpMethod method,
        String body,
        Map<String, String> headers
    ) {
        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(httpHeaders::add);

        HttpEntity<String> entity = new HttpEntity<>(body, httpHeaders);

        try {
            return restTemplate.exchange(targetUrl, method, entity, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body("Service unavailable: " + e.getMessage());
        }
    }
}
