package com.br.oficina.interfaces.rest;

import com.br.oficina.application.produto.*;
import com.br.oficina.domain.produto.Produto;
import com.br.oficina.interfaces.rest.dto.ProdutoRequest;
import com.br.oficina.interfaces.rest.dto.ProdutoResponse;
import com.br.oficina.interfaces.rest.mapper.ProdutoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final CreateProdutoHandler createProdutoHandler;
    private final UpdateProdutoHandler updateProdutoHandler;
    private final DeleteProdutoHandler deleteProdutoHandler;
    private final GetProdutoByIdHandler getProdutoByIdHandler;
    private final ListProdutosHandler listProdutosHandler;
    private final ProdutoMapper mapper;

    @PostMapping
    public ResponseEntity<ProdutoResponse> create(@Valid @RequestBody ProdutoRequest request) {
        Produto produto = mapper.toEntity(request);
        Produto saved = createProdutoHandler.handle(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> list() {
        List<Produto> produtos = listProdutosHandler.handle();
        return ResponseEntity.ok(mapper.toResponseList(produtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> getById(@PathVariable UUID id) {
        Produto produto = getProdutoByIdHandler.handle(id);
        return ResponseEntity.ok(mapper.toResponse(produto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> update(@PathVariable UUID id, @Valid @RequestBody ProdutoRequest request) {
        Produto produto = mapper.toEntity(request);
        Produto updated = updateProdutoHandler.handle(id, produto);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteProdutoHandler.handle(id);
        return ResponseEntity.noContent().build();
    }
}
