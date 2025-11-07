package com.br.oficina.interfaces.rest.dto;

import com.br.oficina.domain.venda.vo.TipoItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVendaRequest {

    @NotNull(message = "Tipo de item é obrigatório")
    private TipoItem tipoItem;

    @NotNull(message = "Item ID é obrigatório")
    private UUID itemId;

    @NotBlank(message = "Nome do item é obrigatório")
    private String itemNome;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser positiva")
    private Integer quantidade;

    @NotNull(message = "Preço unitário é obrigatório")
    @Positive(message = "Preço unitário deve ser positivo")
    private BigDecimal precoUnitario;
}
