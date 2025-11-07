package com.br.oficina.application.relatorio;

import com.br.oficina.domain.venda.VendaRepository;
import com.br.oficina.domain.venda.Venda;
import com.br.oficina.interfaces.rest.dto.RelatorioVendasPorPeriodoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioVendasPorPeriodoHandler {

    private final VendaRepository vendaRepository;

    public RelatorioVendasPorPeriodoResponse handle(LocalDateTime inicio, LocalDateTime fim) {
        List<Venda> vendas = vendaRepository.findByDataVendaBetween(inicio, fim);

        List<Venda> vendasNaoCanceladas = vendas.stream()
                .filter(v -> !v.getCancelada())
                .toList();

        long totalVendas = vendasNaoCanceladas.size();
        BigDecimal valorTotal = vendasNaoCanceladas.stream()
                .map(Venda::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RelatorioVendasPorPeriodoResponse(inicio, fim, totalVendas, valorTotal);
    }
}
