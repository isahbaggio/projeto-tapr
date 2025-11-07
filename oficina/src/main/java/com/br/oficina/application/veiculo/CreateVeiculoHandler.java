package com.br.oficina.application.veiculo;

import com.br.oficina.domain.veiculo.VeiculoRepository;
import com.br.oficina.domain.veiculo.Veiculo;
import com.br.oficina.domain.cliente.ClienteRepository;
import com.br.oficina.infrastructure.exception.NegocioException;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateVeiculoHandler {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;

    @Transactional
    public Veiculo handle(Veiculo veiculo) {
        if (!clienteRepository.existsById(veiculo.getClienteId())) {
            throw new EntidadeNaoEncontradaException("Cliente não encontrado");
        }
        if (veiculoRepository.existsByPlaca(veiculo.getPlaca())) {
            throw new NegocioException("Já existe um veículo com esta placa");
        }
        return veiculoRepository.save(veiculo);
    }
}
