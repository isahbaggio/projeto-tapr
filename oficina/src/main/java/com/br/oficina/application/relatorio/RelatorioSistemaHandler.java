package com.br.oficina.application.relatorio;

import com.br.oficina.domain.cliente.ClienteRepository;
import com.br.oficina.domain.produto.ProdutoRepository;
import com.br.oficina.domain.servico.ServicoRepository;
import com.br.oficina.interfaces.rest.dto.RelatorioSistemaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RelatorioSistemaHandler {

    private final ProdutoRepository produtoRepository;
    private final ServicoRepository servicoRepository;
    private final ClienteRepository clienteRepository;

    public RelatorioSistemaResponse handle() {
        BigDecimal receitaProdutos = produtoRepository.sumReceitaPotencial();
        BigDecimal receitaServicos = servicoRepository.sumReceitaPotencial();
        BigDecimal receitaTotal = receitaProdutos.add(receitaServicos);

        return new RelatorioSistemaResponse(
                produtoRepository.countAll(),
                servicoRepository.countAll(),
                clienteRepository.countAll(),
                receitaTotal,
                receitaProdutos,
                receitaServicos
        );
    }
}
