package com.br.oficina.application.relatorio;

import com.br.oficina.domain.produto.ProdutoRepository;
import com.br.oficina.interfaces.rest.dto.RelatorioEstatisticasProdutosResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RelatorioEstatisticasProdutosHandler {

    private final ProdutoRepository produtoRepository;

    public RelatorioEstatisticasProdutosResponse handle() {
        return new RelatorioEstatisticasProdutosResponse(
                produtoRepository.countAll(),
                produtoRepository.countByAtivo(true),
                produtoRepository.countByAtivo(false),
                produtoRepository.findPrecoMedio(),
                produtoRepository.findPrecoMaximo(),
                produtoRepository.findPrecoMinimo(),
                produtoRepository.countByCategoria()
        );
    }
}
