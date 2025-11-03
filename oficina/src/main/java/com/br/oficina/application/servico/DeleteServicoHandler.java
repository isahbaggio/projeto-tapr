package com.br.oficina.application.servico;

import com.br.oficina.domain.servico.ServicoRepository;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteServicoHandler {

    private final ServicoRepository servicoRepository;

    @Transactional
    public void handle(UUID id) {
        if (!servicoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Serviço não encontrado");
        }
        servicoRepository.deleteById(id);
    }
}
