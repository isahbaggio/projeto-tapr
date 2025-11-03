package com.br.oficina.application.produto;

import com.br.oficina.domain.produto.ProdutoRepository;
import com.br.oficina.domain.produto.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProdutoHandlerTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private CreateProdutoHandler createProdutoHandler;

    private Produto produto;

    @BeforeEach
    void setUp() {
        produto = new Produto();
        produto.setNome("Shampoo");
        produto.setDescricao("Shampoo automotivo");
        produto.setPreco(new BigDecimal("45.90"));
        produto.setCategoria("Limpeza");
        produto.setAtivo(true);
    }

    @Test
    void deveCriarProdutoComSucesso() {
        Produto produtoSalvo = new Produto();
        produtoSalvo.setId(UUID.randomUUID());
        produtoSalvo.setNome(produto.getNome());
        produtoSalvo.setDescricao(produto.getDescricao());
        produtoSalvo.setPreco(produto.getPreco());
        produtoSalvo.setCategoria(produto.getCategoria());
        produtoSalvo.setAtivo(produto.getAtivo());

        when(produtoRepository.save(any(Produto.class))).thenReturn(produtoSalvo);

        Produto resultado = createProdutoHandler.handle(produto);

        assertNotNull(resultado);
        assertNotNull(resultado.getId());
        assertEquals(produto.getNome(), resultado.getNome());
        verify(produtoRepository, times(1)).save(produto);
    }
}
