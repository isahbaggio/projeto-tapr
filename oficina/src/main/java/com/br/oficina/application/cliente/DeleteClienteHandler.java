package com.br.oficina.application.cliente;

import com.br.oficina.domain.cliente.ClienteRepository;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteClienteHandler {

    private final ClienteRepository clienteRepository;

    @Transactional
    public void handle(UUID id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Cliente não encontrado");
        }
        clienteRepository.deleteById(id);
    }
}
