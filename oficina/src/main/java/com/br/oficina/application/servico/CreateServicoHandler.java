package com.br.oficina.application.servico;

import com.br.oficina.domain.servico.ServicoRepository;
import com.br.oficina.domain.servico.Servico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateServicoHandler {

    private final ServicoRepository servicoRepository;

    @Transactional
    public Servico handle(Servico servico) {
        return servicoRepository.save(servico);
    }
}
