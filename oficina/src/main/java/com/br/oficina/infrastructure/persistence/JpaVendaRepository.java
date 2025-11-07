package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.venda.VendaRepository;
import com.br.oficina.domain.venda.Venda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaVendaRepository implements VendaRepository {

    private final SpringDataVendaJpa jpaRepository;

    @Override
    public Venda save(Venda venda) {
        return jpaRepository.save(venda);
    }

    @Override
    public Optional<Venda> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Venda> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Venda> findByClienteId(UUID clienteId) {
        return jpaRepository.findByClienteId(clienteId);
    }

    @Override
    public List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim) {
        return jpaRepository.findByDataVendaBetween(inicio, fim);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }
}
