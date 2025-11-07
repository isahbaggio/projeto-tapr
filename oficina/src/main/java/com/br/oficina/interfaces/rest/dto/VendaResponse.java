package com.br.oficina.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaResponse {
    private UUID id;
    private UUID clienteId;
    private LocalDateTime dataVenda;
    private List<ItemVendaResponse> itens;
    private BigDecimal valorTotal;
    private String observacoes;
    private Boolean cancelada;
}
