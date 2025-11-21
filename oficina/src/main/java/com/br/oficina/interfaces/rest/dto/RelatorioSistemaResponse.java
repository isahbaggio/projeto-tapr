package com.br.oficina.interfaces.rest.dto;

import java.math.BigDecimal;

public record RelatorioSistemaResponse(
    Long totalProdutos,
    Long totalServicos,
    Long totalClientes,
    BigDecimal receitaPotencialTotal,
    BigDecimal receitaPotencialProdutos,
    BigDecimal receitaPotencialServicos
) {}
