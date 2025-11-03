package com.br.oficina.application.produto;

import com.br.oficina.domain.produto.ProdutoRepository;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteProdutoHandler {

    private final ProdutoRepository produtoRepository;

    @Transactional
    public void handle(UUID id) {
        if (!produtoRepository.existsById(id)) {
            throw new EntidadeNaoEncontradaException("Produto não encontrado");
        }
        produtoRepository.deleteById(id);
    }
}
