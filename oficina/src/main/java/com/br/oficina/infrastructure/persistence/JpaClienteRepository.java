package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.cliente.ClienteRepository;
import com.br.oficina.domain.cliente.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaClienteRepository implements ClienteRepository {

    private final SpringDataClienteJpa jpaRepository;

    @Override
    public Cliente save(Cliente cliente) {
        return jpaRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Cliente> findAll() {
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

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return jpaRepository.existsByCpf(cpf);
    }
}
