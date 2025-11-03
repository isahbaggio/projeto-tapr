package com.br.oficina.interfaces.rest;

import com.br.oficina.domain.produto.Produto;
import com.br.oficina.infrastructure.persistence.SpringDataProdutoJpa;
import com.br.oficina.interfaces.rest.dto.ProdutoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProdutoControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SpringDataProdutoJpa produtoRepository;

    @BeforeEach
    void setUp() {
        produtoRepository.deleteAll();
    }

    @Test
    void deveCriarProduto() {
        ProdutoRequest request = new ProdutoRequest();
        request.setNome("Pneu");
        request.setDescricao("Pneu 185/65 R15");
        request.setPreco(new BigDecimal("320.00"));
        request.setCategoria("Pneus");
        request.setAtivo(true);

        ResponseEntity<Produto> response = restTemplate.postForEntity(
                "/produtos",
                request,
                Produto.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Pneu", response.getBody().getNome());
    }

    @Test
    void deveListarProdutos() {
        Produto produto = new Produto();
        produto.setNome("Óleo");
        produto.setDescricao("Óleo 5W30");
        produto.setPreco(new BigDecimal("65.00"));
        produto.setCategoria("Lubrificantes");
        produto.setAtivo(true);
        produtoRepository.save(produto);

        ResponseEntity<String> response = restTemplate.getForEntity("/produtos", String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Óleo"));
    }

    @Test
    void deveDeletarProduto() {
        Produto produto = new Produto();
        produto.setNome("Vela");
        produto.setDescricao("Vela de ignição");
        produto.setPreco(new BigDecimal("28.50"));
        produto.setCategoria("Peças");
        produto.setAtivo(true);
        Produto salvo = produtoRepository.save(produto);

        ResponseEntity<Void> response = restTemplate.exchange(
                "/produtos/" + salvo.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse(produtoRepository.existsById(salvo.getId()));
    }
}
