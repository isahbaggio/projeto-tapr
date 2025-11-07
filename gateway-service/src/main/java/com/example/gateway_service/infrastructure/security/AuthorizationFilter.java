package com.example.gateway_service.infrastructure.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.gateway_service.domain.user.vo.RoleType;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthorizationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationFilter.class);

    private final String jwtSecret;

    private static final Map<String, RoleType> routeRoles = Map.ofEntries(
        Map.entry("/oficina/", RoleType.MECANICO)
    );

    public AuthorizationFilter(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    private boolean isAuthorized(String path, RoleType role) {
        for (Map.Entry<String, RoleType> entry : routeRoles.entrySet()) {
            if (path.startsWith(entry.getKey())) {
                return role.covers(entry.getValue());
            }
        }

        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();

        log.debug("Authorization filter processing request: {} {}", httpRequest.getMethod(), path);

        if (routeRoles.entrySet().stream().noneMatch(entry -> path.startsWith(entry.getKey()))) {
            log.debug("Path {} does not require authorization, allowing request", path);
            chain.doFilter(request, response);
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Missing or invalid Authorization header for path {}", path);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String token = authHeader.substring(7);
        DecodedJWT jwt;
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes(StandardCharsets.UTF_8));
            JWTVerifier verifier = JWT.require(algorithm).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
            log.warn("Invalid JWT token for path {}: {}", path, e.getMessage());
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String tokenType = jwt.getClaim("type").asString();
        if (!tokenType.equals("access")) {
            log.warn("Invalid token type '{}' for path {}", tokenType, path);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String userRoleType = jwt.getClaim("role").asString();
        RoleType role;
        try {
            role = RoleType.valueOf(userRoleType);
        } catch (Exception e) {
            log.warn("Invalid role '{}' for path {}", userRoleType, path);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!isAuthorized(path, role)) {
            log.warn("User with role {} is not authorized for path {}", role, path);
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        log.debug("User with role {} authorized for path {}", role, path);
        chain.doFilter(request, response);
    }
}
