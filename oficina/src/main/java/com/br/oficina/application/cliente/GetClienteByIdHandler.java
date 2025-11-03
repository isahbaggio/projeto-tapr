package com.br.oficina.application.cliente;

import com.br.oficina.domain.cliente.ClienteRepository;
import com.br.oficina.domain.cliente.Cliente;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetClienteByIdHandler {

    private final ClienteRepository clienteRepository;

    public Cliente handle(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));
    }
}
