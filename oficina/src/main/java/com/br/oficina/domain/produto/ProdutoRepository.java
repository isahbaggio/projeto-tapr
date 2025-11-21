package com.br.oficina.domain.produto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ProdutoRepository {
    Produto save(Produto produto);
    Optional<Produto> findById(UUID id);
    List<Produto> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);

    // Métodos para relatórios
    Long countAll();
    Long countByAtivo(Boolean ativo);
    BigDecimal findPrecoMedio();
    BigDecimal findPrecoMaximo();
    BigDecimal findPrecoMinimo();
    Map<String, Long> countByCategoria();
    BigDecimal sumReceitaPotencial();
}
