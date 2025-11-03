package com.br.oficina.interfaces.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponse {
    private UUID id;
    private String nome;
    private String telefone;
    private String email;
    private String cpf;
    private String endereco;
    private Boolean ativo;
}
