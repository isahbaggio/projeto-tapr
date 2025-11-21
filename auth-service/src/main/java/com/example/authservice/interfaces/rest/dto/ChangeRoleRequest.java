package com.example.authservice.interfaces.rest.dto;

import com.example.authservice.domain.user.vo.RoleType;
import jakarta.validation.constraints.NotNull;

public record ChangeRoleRequest(
        @NotNull RoleType role
) {
}
