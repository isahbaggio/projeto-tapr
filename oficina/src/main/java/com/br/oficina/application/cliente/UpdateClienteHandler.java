package com.br.oficina.application.cliente;

import com.br.oficina.domain.cliente.ClienteRepository;
import com.br.oficina.domain.cliente.Cliente;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateClienteHandler {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente handle(UUID id, Cliente clienteAtualizado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Cliente não encontrado"));

        cliente.setNome(clienteAtualizado.getNome());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setCpf(clienteAtualizado.getCpf());
        cliente.setEndereco(clienteAtualizado.getEndereco());
        cliente.setAtivo(clienteAtualizado.getAtivo());

        return clienteRepository.save(cliente);
    }
}
