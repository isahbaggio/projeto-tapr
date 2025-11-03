package com.br.oficina.application.servico;

import com.br.oficina.domain.servico.ServicoRepository;
import com.br.oficina.domain.servico.Servico;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateServicoHandler {

    private final ServicoRepository servicoRepository;

    @Transactional
    public Servico handle(UUID id, Servico servicoAtualizado) {
        Servico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Serviço não encontrado"));

        servico.setNome(servicoAtualizado.getNome());
        servico.setDescricao(servicoAtualizado.getDescricao());
        servico.setPreco(servicoAtualizado.getPreco());
        servico.setDuracao(servicoAtualizado.getDuracao());
        servico.setAtivo(servicoAtualizado.getAtivo());

        return servicoRepository.save(servico);
    }
}
