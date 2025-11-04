package com.example.gateway_service.application.proxy;

import com.example.gateway_service.application.ports.HttpForwarder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProxyAuthHandler {

    @Value("${backend.auth-service.url}")
    private String authServiceUrl;

    private final HttpForwarder httpForwarder;

    public ResponseEntity<String> handle(
        String requestPath,
        HttpMethod method,
        String body,
        Map<String, String> headers
    ) {
        String targetUrl = authServiceUrl + requestPath;
        return httpForwarder.forward(targetUrl, method, body, headers);
    }
}
