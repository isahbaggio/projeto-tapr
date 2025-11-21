package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.servico.ServicoRepository;
import com.br.oficina.domain.servico.Servico;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaServicoRepository implements ServicoRepository {

    private final SpringDataServicoJpa jpaRepository;

    @Override
    public Servico save(Servico servico) {
        return jpaRepository.save(servico);
    }

    @Override
    public Optional<Servico> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Servico> findAll() {
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
    public Double findDuracaoMedia() {
        Double duracao = jpaRepository.findDuracaoMedia();
        return duracao != null ? duracao : 0.0;
    }

    @Override
    public Map<String, Long> countByFaixaDuracao() {
        List<Servico> servicos = jpaRepository.findAll();
        Map<String, Long> faixas = new LinkedHashMap<>();

        faixas.put("0-30 min", servicos.stream().filter(s -> s.getDuracao() <= 30).count());
        faixas.put("30-60 min", servicos.stream().filter(s -> s.getDuracao() > 30 && s.getDuracao() <= 60).count());
        faixas.put("60-120 min", servicos.stream().filter(s -> s.getDuracao() > 60 && s.getDuracao() <= 120).count());
        faixas.put("120+ min", servicos.stream().filter(s -> s.getDuracao() > 120).count());

        return faixas;
    }

    @Override
    public BigDecimal sumReceitaPotencial() {
        BigDecimal receita = jpaRepository.sumReceitaPotencial();
        return receita != null ? receita : BigDecimal.ZERO;
    }
}
