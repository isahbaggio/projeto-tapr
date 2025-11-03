package com.br.oficina.application.produto;

import com.br.oficina.domain.produto.ProdutoRepository;
import com.br.oficina.domain.produto.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateProdutoHandler {

    private final ProdutoRepository produtoRepository;

    @Transactional
    public Produto handle(Produto produto) {
        return produtoRepository.save(produto);
    }
}
