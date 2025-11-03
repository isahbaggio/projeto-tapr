package com.br.oficina.interfaces.rest;

import com.br.oficina.application.servico.*;
import com.br.oficina.domain.servico.Servico;
import com.br.oficina.interfaces.rest.dto.ServicoRequest;
import com.br.oficina.interfaces.rest.dto.ServicoResponse;
import com.br.oficina.interfaces.rest.mapper.ServicoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoController {

    private final CreateServicoHandler createServicoHandler;
    private final UpdateServicoHandler updateServicoHandler;
    private final DeleteServicoHandler deleteServicoHandler;
    private final GetServicoByIdHandler getServicoByIdHandler;
    private final ListServicosHandler listServicosHandler;
    private final ServicoMapper mapper;

    @PostMapping
    public ResponseEntity<ServicoResponse> create(@Valid @RequestBody ServicoRequest request) {
        Servico servico = mapper.toEntity(request);
        Servico saved = createServicoHandler.handle(servico);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<ServicoResponse>> list() {
        List<Servico> servicos = listServicosHandler.handle();
        return ResponseEntity.ok(mapper.toResponseList(servicos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponse> getById(@PathVariable UUID id) {
        Servico servico = getServicoByIdHandler.handle(id);
        return ResponseEntity.ok(mapper.toResponse(servico));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponse> update(@PathVariable UUID id, @Valid @RequestBody ServicoRequest request) {
        Servico servico = mapper.toEntity(request);
        Servico updated = updateServicoHandler.handle(id, servico);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteServicoHandler.handle(id);
        return ResponseEntity.noContent().build();
    }
}
