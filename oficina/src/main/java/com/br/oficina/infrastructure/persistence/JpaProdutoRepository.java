package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.produto.ProdutoRepository;
import com.br.oficina.domain.produto.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Override
    public Long countAll() {
        return jpaRepository.count();
    }

    @Override
    public Long countByAtivo(Boolean ativo) {
        return jpaRepository.countByAtivo(ativo);
    }

    @Override
    public BigDecimal findPrecoMedio() {
        BigDecimal preco = jpaRepository.findPrecoMedio();
        return preco != null ? preco : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal findPrecoMaximo() {
        BigDecimal preco = jpaRepository.findPrecoMaximo();
        return preco != null ? preco : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal findPrecoMinimo() {
        BigDecimal preco = jpaRepository.findPrecoMinimo();
        return preco != null ? preco : BigDecimal.ZERO;
    }

    @Override
    public Map<String, Long> countByCategoria() {
        return jpaRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Produto::getCategoria,
                        Collectors.counting()
                ));
    }

    @Override
    public BigDecimal sumReceitaPotencial() {
        BigDecimal receita = jpaRepository.sumReceitaPotencial();
        return receita != null ? receita : BigDecimal.ZERO;
    }
}
