package com.br.oficina.application.relatorio;

import com.br.oficina.domain.servico.ServicoRepository;
import com.br.oficina.interfaces.rest.dto.RelatorioEstatisticasServicosResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelatorioEstatisticasServicosHandler {

    private final ServicoRepository servicoRepository;

    public RelatorioEstatisticasServicosResponse handle() {
        return new RelatorioEstatisticasServicosResponse(
                servicoRepository.countAll(),
                servicoRepository.countByAtivo(true),
                servicoRepository.countByAtivo(false),
                servicoRepository.findPrecoMedio(),
                servicoRepository.findPrecoMaximo(),
                servicoRepository.findDuracaoMedia(),
                servicoRepository.countByFaixaDuracao()
        );
    }
}
