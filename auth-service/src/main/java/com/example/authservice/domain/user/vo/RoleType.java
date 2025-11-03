package com.example.authservice.domain.user.vo;

import lombok.Getter;

@Getter
public enum RoleType {
    CLIENTE(1),
    ATENDENTE(2),
    MECANICO(3),
    GESTOR(4);

    private final int level;

    RoleType(int level) {
        this.level = level;
    }

    public boolean covers(RoleType other) {
        return this.level >= other.level;
    }
}
