package com.br.oficina.domain.servico;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ServicoRepository {
    Servico save(Servico servico);
    Optional<Servico> findById(UUID id);
    List<Servico> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);

    // Métodos para relatórios
    Long countAll();
    Long countByAtivo(Boolean ativo);
    BigDecimal findPrecoMedio();
    BigDecimal findPrecoMaximo();
    Double findDuracaoMedia();
    Map<String, Long> countByFaixaDuracao();
    BigDecimal sumReceitaPotencial();
}
