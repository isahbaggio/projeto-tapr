package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.servico.ServicoRepository;
import com.br.oficina.domain.servico.Servico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaServicoRepository implements ServicoRepository {

    private final SpringDataServicoJpa jpaRepository;

    @Override
    public Servico save(Servico servico) {
        return jpaRepository.save(servico);
    }

    @Override
    public Optional<Servico> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Servico> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }
}
