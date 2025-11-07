package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.veiculo.VeiculoRepository;
import com.br.oficina.domain.veiculo.Veiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaVeiculoRepository implements VeiculoRepository {

    private final SpringDataVeiculoJpa jpaRepository;

    @Override
    public Veiculo save(Veiculo veiculo) {
        return jpaRepository.save(veiculo);
    }

    @Override
    public Optional<Veiculo> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Veiculo> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Veiculo> findByClienteId(UUID clienteId) {
        return jpaRepository.findByClienteId(clienteId);
    }

    @Override
    public void delete(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByPlaca(String placa) {
        return jpaRepository.existsByPlaca(placa);
    }
}
