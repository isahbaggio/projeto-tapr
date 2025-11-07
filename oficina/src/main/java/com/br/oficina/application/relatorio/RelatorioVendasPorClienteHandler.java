package com.br.oficina.application.relatorio;

import com.br.oficina.domain.cliente.ClienteRepository;
import com.br.oficina.infrastructure.persistence.SpringDataItemVendaJpa;
import com.br.oficina.interfaces.rest.dto.RelatorioVendasPorClienteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RelatorioVendasPorClienteHandler {

    private final SpringDataItemVendaJpa itemVendaRepository;
    private final ClienteRepository clienteRepository;

    public List<RelatorioVendasPorClienteResponse> handle() {
        List<Object[]> results = itemVendaRepository.findVendasPorCliente();

        return results.stream()
                .map(row -> {
                    UUID clienteId = (UUID) row[0];
                    BigDecimal valorTotal = (BigDecimal) row[1];
                    Long totalVendas = (Long) row[2];

                    String clienteNome = clienteRepository.findById(clienteId)
                            .map(c -> c.getNome())
                            .orElse("Cliente não encontrado");

                    return new RelatorioVendasPorClienteResponse(clienteId, clienteNome, totalVendas, valorTotal);
                })
                .toList();
    }
}
