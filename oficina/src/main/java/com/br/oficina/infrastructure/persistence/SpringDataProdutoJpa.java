package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataProdutoJpa extends JpaRepository<Produto, UUID> {
}
