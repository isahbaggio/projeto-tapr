package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.veiculo.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataVeiculoJpa extends JpaRepository<Veiculo, UUID> {
    boolean existsByPlaca(String placa);
    List<Veiculo> findByClienteId(UUID clienteId);
}
