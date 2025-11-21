package com.br.oficina.interfaces.rest.dto;

import java.math.BigDecimal;
import java.util.Map;

public record RelatorioEstatisticasProdutosResponse(
    Long totalProdutos,
    Long produtosAtivos,
    Long produtosInativos,
    BigDecimal precoMedio,
    BigDecimal precoMaisAlto,
    BigDecimal precoMaisBaixo,
    Map<String, Long> produtosPorCategoria
) {}
