package com.br.oficina.interfaces.rest.dto;

import com.br.oficina.domain.venda.vo.TipoItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVendaResponse {
    private UUID id;
    private TipoItem tipoItem;
    private UUID itemId;
    private String itemNome;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
}
