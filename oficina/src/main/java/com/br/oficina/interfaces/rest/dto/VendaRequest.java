package com.br.oficina.interfaces.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendaRequest {

    @NotNull(message = "Cliente ID é obrigatório")
    private UUID clienteId;

    private LocalDateTime dataVenda;

    @NotEmpty(message = "A venda deve conter pelo menos um item")
    @Valid
    private List<ItemVendaRequest> itens;

    private String observacoes;
}
