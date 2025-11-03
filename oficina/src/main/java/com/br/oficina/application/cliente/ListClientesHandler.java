package com.br.oficina.application.cliente;

import com.br.oficina.domain.cliente.ClienteRepository;
import com.br.oficina.domain.cliente.Cliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListClientesHandler {

    private final ClienteRepository clienteRepository;

    public List<Cliente> handle() {
        return clienteRepository.findAll();
    }
}
