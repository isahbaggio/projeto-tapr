package com.br.oficina.interfaces.rest.dto;

import java.math.BigDecimal;
import java.util.Map;

public record RelatorioEstatisticasServicosResponse(
    Long totalServicos,
    Long servicosAtivos,
    Long servicosInativos,
    BigDecimal precoMedio,
    BigDecimal precoMaisAlto,
    Double duracaoMedia,
    Map<String, Long> servicosPorFaixaDuracao
) {}
