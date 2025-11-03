package com.br.oficina.interfaces.rest.mapper;

import com.br.oficina.domain.servico.Servico;
import com.br.oficina.interfaces.rest.dto.ServicoRequest;
import com.br.oficina.interfaces.rest.dto.ServicoResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServicoMapper {
    Servico toEntity(ServicoRequest request);
    ServicoResponse toResponse(Servico servico);
    List<ServicoResponse> toResponseList(List<Servico> servicos);
}
