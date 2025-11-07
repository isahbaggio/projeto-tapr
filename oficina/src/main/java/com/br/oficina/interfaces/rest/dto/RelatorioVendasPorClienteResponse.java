package com.br.oficina.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioVendasPorClienteResponse {
    private UUID clienteId;
    private String clienteNome;
    private Long totalVendas;
    private BigDecimal valorTotal;
}
