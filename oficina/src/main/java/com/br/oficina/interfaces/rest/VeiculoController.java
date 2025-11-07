package com.br.oficina.interfaces.rest;

import com.br.oficina.application.veiculo.*;
import com.br.oficina.domain.veiculo.Veiculo;
import com.br.oficina.interfaces.rest.dto.VeiculoRequest;
import com.br.oficina.interfaces.rest.dto.VeiculoResponse;
import com.br.oficina.interfaces.rest.mapper.VeiculoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/veiculos")
@RequiredArgsConstructor
public class VeiculoController {

    private final CreateVeiculoHandler createVeiculoHandler;
    private final UpdateVeiculoHandler updateVeiculoHandler;
    private final DeleteVeiculoHandler deleteVeiculoHandler;
    private final GetVeiculoByIdHandler getVeiculoByIdHandler;
    private final ListVeiculosHandler listVeiculosHandler;
    private final ListVeiculosByClienteHandler listVeiculosByClienteHandler;
    private final VeiculoMapper mapper;

    @PostMapping
    public ResponseEntity<VeiculoResponse> create(@Valid @RequestBody VeiculoRequest request) {
        Veiculo veiculo = mapper.toEntity(request);
        Veiculo saved = createVeiculoHandler.handle(veiculo);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<VeiculoResponse>> list() {
        List<Veiculo> veiculos = listVeiculosHandler.handle();
        return ResponseEntity.ok(mapper.toResponseList(veiculos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponse> getById(@PathVariable UUID id) {
        Veiculo veiculo = getVeiculoByIdHandler.handle(id);
        return ResponseEntity.ok(mapper.toResponse(veiculo));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VeiculoResponse>> getByClienteId(@PathVariable UUID clienteId) {
        List<Veiculo> veiculos = listVeiculosByClienteHandler.handle(clienteId);
        return ResponseEntity.ok(mapper.toResponseList(veiculos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponse> update(@PathVariable UUID id, @Valid @RequestBody VeiculoRequest request) {
        Veiculo veiculo = mapper.toEntity(request);
        Veiculo updated = updateVeiculoHandler.handle(id, veiculo);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteVeiculoHandler.handle(id);
        return ResponseEntity.noContent().build();
    }
}
