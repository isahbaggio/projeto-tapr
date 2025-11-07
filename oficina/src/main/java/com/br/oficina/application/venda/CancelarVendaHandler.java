package com.br.oficina.application.venda;

import com.br.oficina.domain.venda.VendaRepository;
import com.br.oficina.domain.venda.Venda;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import com.br.oficina.infrastructure.exception.NegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CancelarVendaHandler {

    private final VendaRepository vendaRepository;

    @Transactional
    public void handle(UUID id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Venda não encontrada"));

        if (venda.getCancelada()) {
            throw new NegocioException("Venda já está cancelada");
        }

        venda.setCancelada(true);
        vendaRepository.save(venda);
    }
}
