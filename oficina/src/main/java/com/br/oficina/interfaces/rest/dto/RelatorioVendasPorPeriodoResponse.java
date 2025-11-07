package com.br.oficina.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatorioVendasPorPeriodoResponse {
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private Long totalVendas;
    private BigDecimal valorTotal;
}
