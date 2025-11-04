package com.example.gateway_service.domain.user.vo;


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

    public int getLevel() {
        return this.level;
    }
}
