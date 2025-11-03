package com.br.oficina.application.cliente;

import com.br.oficina.domain.cliente.ClienteRepository;
import com.br.oficina.domain.cliente.Cliente;
import com.br.oficina.infrastructure.exception.NegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateClienteHandler {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente handle(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new NegocioException("Já existe um cliente com este email");
        }
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new NegocioException("Já existe um cliente com este CPF");
        }
        return clienteRepository.save(cliente);
    }
}
