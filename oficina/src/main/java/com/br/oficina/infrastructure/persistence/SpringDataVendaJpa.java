package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.venda.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface SpringDataVendaJpa extends JpaRepository<Venda, UUID> {
    List<Venda> findByClienteId(UUID clienteId);
    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);
}
