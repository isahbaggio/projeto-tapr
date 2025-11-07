package com.br.oficina.application.venda;

import com.br.oficina.domain.venda.VendaRepository;
import com.br.oficina.domain.venda.Venda;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetVendaByIdHandler {

    private final VendaRepository vendaRepository;

    public Venda handle(UUID id) {
        return vendaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada"));
    }
}
