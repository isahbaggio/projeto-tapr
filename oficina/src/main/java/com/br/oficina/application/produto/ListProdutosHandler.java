package com.br.oficina.application.produto;

import com.br.oficina.domain.produto.ProdutoRepository;
import com.br.oficina.domain.produto.Produto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListProdutosHandler {

    private final ProdutoRepository produtoRepository;

    public List<Produto> handle() {
        return produtoRepository.findAll();
    }
}
