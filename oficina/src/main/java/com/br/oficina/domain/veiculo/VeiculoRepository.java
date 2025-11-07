package com.br.oficina.domain.veiculo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VeiculoRepository {
    Veiculo save(Veiculo veiculo);
    Optional<Veiculo> findById(UUID id);
    List<Veiculo> findAll();
    List<Veiculo> findByClienteId(UUID clienteId);
    void delete(UUID id);
    boolean existsByPlaca(String placa);
}
