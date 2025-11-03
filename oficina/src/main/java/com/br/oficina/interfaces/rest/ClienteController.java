package com.br.oficina.interfaces.rest;

import com.br.oficina.application.cliente.*;
import com.br.oficina.domain.cliente.Cliente;
import com.br.oficina.interfaces.rest.dto.ClienteRequest;
import com.br.oficina.interfaces.rest.dto.ClienteResponse;
import com.br.oficina.interfaces.rest.mapper.ClienteMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final CreateClienteHandler createClienteHandler;
    private final UpdateClienteHandler updateClienteHandler;
    private final DeleteClienteHandler deleteClienteHandler;
    private final GetClienteByIdHandler getClienteByIdHandler;
    private final ListClientesHandler listClientesHandler;
    private final ClienteMapper mapper;

    @PostMapping
    public ResponseEntity<ClienteResponse> create(@Valid @RequestBody ClienteRequest request) {
        Cliente cliente = mapper.toEntity(request);
        Cliente saved = createClienteHandler.handle(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> list() {
        List<Cliente> clientes = listClientesHandler.handle();
        return ResponseEntity.ok(mapper.toResponseList(clientes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> getById(@PathVariable UUID id) {
        Cliente cliente = getClienteByIdHandler.handle(id);
        return ResponseEntity.ok(mapper.toResponse(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> update(@PathVariable UUID id, @Valid @RequestBody ClienteRequest request) {
        Cliente cliente = mapper.toEntity(request);
        Cliente updated = updateClienteHandler.handle(id, cliente);
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteClienteHandler.handle(id);
        return ResponseEntity.noContent().build();
    }
}
