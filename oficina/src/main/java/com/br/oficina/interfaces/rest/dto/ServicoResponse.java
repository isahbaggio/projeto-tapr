package com.br.oficina.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicoResponse {
    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer duracao;
    private Boolean ativo;
}
