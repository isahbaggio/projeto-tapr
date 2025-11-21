package com.br.oficina.interfaces.rest.dto;

public record RelatorioEstatisticasClientesResponse(
    Long totalClientes,
    Long clientesAtivos,
    Long clientesInativos
) {}
