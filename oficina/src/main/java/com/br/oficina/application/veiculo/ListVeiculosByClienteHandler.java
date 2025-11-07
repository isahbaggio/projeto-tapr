package com.br.oficina.application.veiculo;

import com.br.oficina.domain.veiculo.VeiculoRepository;
import com.br.oficina.domain.veiculo.Veiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListVeiculosByClienteHandler {

    private final VeiculoRepository veiculoRepository;

    public List<Veiculo> handle(UUID clienteId) {
        return veiculoRepository.findByClienteId(clienteId);
    }
}
