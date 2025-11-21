package com.example.authservice.interfaces.rest;

import com.example.authservice.application.user.*;
import com.example.authservice.interfaces.rest.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/users")
public class UserController {
    private final ListUserHandler listUserHandler;
    private final RegisterUserHandler registerUserHandler;
    private final GetUserByIdHandler getUserByIdHandler;
    private final UpdateUserHandler updateUserHandler;
    private final ChangeUserRoleHandler changeUserRoleHandler;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> list(Pageable pageable) {
        Page<UserResponse> users = listUserHandler.handle(pageable);

        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        UserResponse created = registerUserHandler.handle(
                request.name(),
                request.email(),
                request.password(),
                request.role()
        );

        return ResponseEntity
                .created(URI.create("/users/" + created.id()))
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable UUID id) {
        UserResponse user = getUserByIdHandler.handle(id);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse updated = updateUserHandler.handle(
                id,
                request.name(),
                request.email()
        );

        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> changeRole(
            @PathVariable String id,
            @Valid @RequestBody ChangeRoleRequest request) {
        UserResponse updated = changeUserRoleHandler.handle(id, request.role());

        return ResponseEntity.ok(updated);
    }
}
