package com.br.oficina.application.relatorio;

import com.br.oficina.domain.venda.vo.TipoItem;
import com.br.oficina.infrastructure.persistence.SpringDataItemVendaJpa;
import com.br.oficina.interfaces.rest.dto.RelatorioItemMaisVendidoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RelatorioProdutosMaisVendidosHandler {

    private final SpringDataItemVendaJpa itemVendaRepository;

    public List<RelatorioItemMaisVendidoResponse> handle() {
        List<Object[]> results = itemVendaRepository.findMaisVendidosByTipo(TipoItem.PRODUTO);

        return results.stream()
                .map(row -> new RelatorioItemMaisVendidoResponse(
                        (UUID) row[0],
                        (String) row[1],
                        (TipoItem) row[2],
                        (Long) row[3],
                        (BigDecimal) row[4]
                ))
                .toList();
    }
}
