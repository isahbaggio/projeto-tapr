package com.br.oficina.application.veiculo;

import com.br.oficina.domain.veiculo.VeiculoRepository;
import com.br.oficina.domain.veiculo.Veiculo;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetVeiculoByIdHandler {

    private final VeiculoRepository veiculoRepository;

    public Veiculo handle(UUID id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado"));
    }
}
