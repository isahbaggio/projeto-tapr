package com.br.oficina.interfaces.rest.mapper;

import com.br.oficina.domain.cliente.Cliente;
import com.br.oficina.interfaces.rest.dto.ClienteRequest;
import com.br.oficina.interfaces.rest.dto.ClienteResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {
    Cliente toEntity(ClienteRequest request);
    ClienteResponse toResponse(Cliente cliente);
    List<ClienteResponse> toResponseList(List<Cliente> clientes);
}
