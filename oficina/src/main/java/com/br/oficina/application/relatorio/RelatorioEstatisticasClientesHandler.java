package com.br.oficina.application.relatorio;

import com.br.oficina.domain.cliente.ClienteRepository;
import com.br.oficina.interfaces.rest.dto.RelatorioEstatisticasClientesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelatorioEstatisticasClientesHandler {

    private final ClienteRepository clienteRepository;

    public RelatorioEstatisticasClientesResponse handle() {
        return new RelatorioEstatisticasClientesResponse(
                clienteRepository.countAll(),
                clienteRepository.countByAtivo(true),
                clienteRepository.countByAtivo(false)
        );
    }
}
