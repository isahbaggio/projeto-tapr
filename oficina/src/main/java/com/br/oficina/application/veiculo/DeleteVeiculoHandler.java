package com.br.oficina.application.veiculo;

import com.br.oficina.domain.veiculo.VeiculoRepository;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteVeiculoHandler {

    private final VeiculoRepository veiculoRepository;

    @Transactional
    public void handle(UUID id) {
        if (!veiculoRepository.findById(id).isPresent()) {
            throw new EntidadeNaoEncontradaException("Veículo não encontrado");
        }
        veiculoRepository.delete(id);
    }
}
