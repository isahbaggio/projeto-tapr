package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.servico.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.UUID;

public interface SpringDataServicoJpa extends JpaRepository<Servico, UUID> {

    Long countByAtivo(Boolean ativo);

    @Query("SELECT AVG(s.preco) FROM Servico s")
    BigDecimal findPrecoMedio();

    @Query("SELECT MAX(s.preco) FROM Servico s")
    BigDecimal findPrecoMaximo();

    @Query("SELECT AVG(s.duracao) FROM Servico s")
    Double findDuracaoMedia();

    @Query("SELECT SUM(s.preco) FROM Servico s")
    BigDecimal sumReceitaPotencial();
}
