package com.br.oficina.domain.cliente;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository {
    Cliente save(Cliente cliente);
    Optional<Cliente> findById(UUID id);
    List<Cliente> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

    // Métodos para relatórios
    Long countAll();
    Long countByAtivo(Boolean ativo);
}
