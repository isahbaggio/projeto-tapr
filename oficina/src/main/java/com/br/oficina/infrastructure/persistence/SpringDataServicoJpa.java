package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.servico.Servico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataServicoJpa extends JpaRepository<Servico, UUID> {
}
