package com.example.gateway_service.application.ports;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface HttpForwarder {
    ResponseEntity<String> forward(
        String targetUrl,
        HttpMethod method,
        String body,
        Map<String, String> headers
    );
}
