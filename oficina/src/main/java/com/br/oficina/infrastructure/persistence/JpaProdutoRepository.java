package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.produto.ProdutoRepository;
import com.br.oficina.domain.produto.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaProdutoRepository implements ProdutoRepository {

    private final SpringDataProdutoJpa jpaRepository;

    @Override
    public Produto save(Produto produto) {
        return jpaRepository.save(produto);
    }

    @Override
    public Optional<Produto> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Produto> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaRepository.existsById(id);
    }
}
