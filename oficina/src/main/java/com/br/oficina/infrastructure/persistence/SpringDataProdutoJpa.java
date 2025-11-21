package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.UUID;

public interface SpringDataProdutoJpa extends JpaRepository<Produto, UUID> {

    Long countByAtivo(Boolean ativo);

    @Query("SELECT AVG(p.preco) FROM Produto p")
    BigDecimal findPrecoMedio();

    @Query("SELECT MAX(p.preco) FROM Produto p")
    BigDecimal findPrecoMaximo();

    @Query("SELECT MIN(p.preco) FROM Produto p")
    BigDecimal findPrecoMinimo();

    @Query("SELECT SUM(p.preco) FROM Produto p")
    BigDecimal sumReceitaPotencial();
}
