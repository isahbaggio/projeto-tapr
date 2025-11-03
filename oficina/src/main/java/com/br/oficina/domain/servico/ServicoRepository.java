package com.br.oficina.domain.servico;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServicoRepository {
    Servico save(Servico servico);
    Optional<Servico> findById(UUID id);
    List<Servico> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
