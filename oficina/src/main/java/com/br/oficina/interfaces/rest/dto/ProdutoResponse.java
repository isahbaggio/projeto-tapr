package com.br.oficina.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoResponse {
    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String categoria;
    private Boolean ativo;
}
