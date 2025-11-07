package com.br.oficina.application.veiculo;

import com.br.oficina.domain.veiculo.VeiculoRepository;
import com.br.oficina.domain.veiculo.Veiculo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListVeiculosHandler {

    private final VeiculoRepository veiculoRepository;

    public List<Veiculo> handle() {
        return veiculoRepository.findAll();
    }
}
