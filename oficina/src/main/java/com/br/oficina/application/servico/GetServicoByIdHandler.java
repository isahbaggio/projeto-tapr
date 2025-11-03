package com.br.oficina.application.servico;

import com.br.oficina.domain.servico.ServicoRepository;
import com.br.oficina.domain.servico.Servico;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetServicoByIdHandler {

    private final ServicoRepository servicoRepository;

    public Servico handle(UUID id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço não encontrado"));
    }
}
