package com.br.oficina.application.produto;

import com.br.oficina.domain.produto.ProdutoRepository;
import com.br.oficina.domain.produto.Produto;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetProdutoByIdHandler {

    private final ProdutoRepository produtoRepository;

    public Produto handle(UUID id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto não encontrado"));
    }
}
