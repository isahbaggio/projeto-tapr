package com.example.gateway_service.interfaces.rest;

import com.example.gateway_service.application.proxy.ProxyAuthHandler;
import com.example.gateway_service.application.proxy.ProxyOficinaHandler;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProxyController {

    private final ProxyAuthHandler proxyAuthHandler;
    private final ProxyOficinaHandler proxyOficinaHandler;

    @RequestMapping("/auth-service/**")
    public ResponseEntity<String> proxyAuth(
        @RequestBody(required = false) String body,
        HttpMethod method,
        HttpServletRequest request
    ) {
        String path = request.getRequestURI().substring("/auth-service".length());
        Map<String, String> headers = extractHeaders(request);
        return proxyAuthHandler.handle(path, method, body, headers);
    }

    @RequestMapping("/oficina/**")
    public ResponseEntity<String> proxyOficina(
        @RequestBody(required = false) String body,
        HttpMethod method,
        HttpServletRequest request
    ) {
        String path = request.getRequestURI().substring(8);
        Map<String, String> headers = extractHeaders(request);
        return proxyOficinaHandler.handle(path, method, body, headers);
    }

    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!headerName.equalsIgnoreCase("host")) {
                headers.put(headerName, request.getHeader(headerName));
            }
        }
        return headers;
    }
}
