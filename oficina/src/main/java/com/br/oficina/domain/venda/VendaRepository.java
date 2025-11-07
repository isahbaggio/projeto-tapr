package com.br.oficina.domain.venda;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VendaRepository {
    Venda save(Venda venda);
    Optional<Venda> findById(UUID id);
    List<Venda> findAll();
    List<Venda> findByClienteId(UUID clienteId);
    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);
    void delete(UUID id);
}
