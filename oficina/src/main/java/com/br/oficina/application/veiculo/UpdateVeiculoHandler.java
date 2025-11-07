package com.br.oficina.application.veiculo;

import com.br.oficina.domain.veiculo.VeiculoRepository;
import com.br.oficina.domain.veiculo.Veiculo;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateVeiculoHandler {

    private final VeiculoRepository veiculoRepository;

    @Transactional
    public Veiculo handle(UUID id, Veiculo veiculoAtualizado) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Veículo não encontrado"));

        veiculo.setPlaca(veiculoAtualizado.getPlaca());
        veiculo.setMarca(veiculoAtualizado.getMarca());
        veiculo.setModelo(veiculoAtualizado.getModelo());
        veiculo.setAno(veiculoAtualizado.getAno());
        veiculo.setCor(veiculoAtualizado.getCor());
        veiculo.setClienteId(veiculoAtualizado.getClienteId());
        veiculo.setAtivo(veiculoAtualizado.getAtivo());

        return veiculoRepository.save(veiculo);
    }
}
