package com.br.oficina.application.servico;

import com.br.oficina.domain.servico.ServicoRepository;
import com.br.oficina.domain.servico.Servico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListServicosHandler {

    private final ServicoRepository servicoRepository;

    public List<Servico> handle() {
        return servicoRepository.findAll();
    }
}
