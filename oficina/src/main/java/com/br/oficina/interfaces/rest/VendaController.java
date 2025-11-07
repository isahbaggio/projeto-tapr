package com.br.oficina.interfaces.rest;

import com.br.oficina.application.venda.*;
import com.br.oficina.domain.venda.Venda;
import com.br.oficina.interfaces.rest.dto.VendaRequest;
import com.br.oficina.interfaces.rest.dto.VendaResponse;
import com.br.oficina.interfaces.rest.mapper.VendaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final CreateVendaHandler createVendaHandler;
    private final ListVendasHandler listVendasHandler;
    private final GetVendaByIdHandler getVendaByIdHandler;
    private final ListVendasByClienteHandler listVendasByClienteHandler;
    private final CancelarVendaHandler cancelarVendaHandler;
    private final VendaMapper mapper;

    @PostMapping
    public ResponseEntity<VendaResponse> create(@Valid @RequestBody VendaRequest request) {
        Venda venda = mapper.toEntity(request);
        Venda saved = createVendaHandler.handle(venda);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<VendaResponse>> list() {
        List<Venda> vendas = listVendasHandler.handle();
        return ResponseEntity.ok(mapper.toResponseList(vendas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponse> getById(@PathVariable UUID id) {
        Venda venda = getVendaByIdHandler.handle(id);
        return ResponseEntity.ok(mapper.toResponse(venda));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<VendaResponse>> getByClienteId(@PathVariable UUID clienteId) {
        List<Venda> vendas = listVendasByClienteHandler.handle(clienteId);
        return ResponseEntity.ok(mapper.toResponseList(vendas));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable UUID id) {
        cancelarVendaHandler.handle(id);
        return ResponseEntity.noContent().build();
    }
}
