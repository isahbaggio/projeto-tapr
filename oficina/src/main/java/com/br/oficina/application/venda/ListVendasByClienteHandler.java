package com.br.oficina.application.venda;

import com.br.oficina.domain.venda.VendaRepository;
import com.br.oficina.domain.venda.Venda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListVendasByClienteHandler {

    private final VendaRepository vendaRepository;

    public List<Venda> handle(UUID clienteId) {
        return vendaRepository.findByClienteId(clienteId);
    }
}
