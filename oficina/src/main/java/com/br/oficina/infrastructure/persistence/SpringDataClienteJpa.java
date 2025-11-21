package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataClienteJpa extends JpaRepository<Cliente, UUID> {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    Long countByAtivo(Boolean ativo);
}
