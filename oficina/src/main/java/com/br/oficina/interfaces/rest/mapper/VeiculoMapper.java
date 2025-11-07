package com.br.oficina.interfaces.rest.mapper;

import com.br.oficina.domain.veiculo.Veiculo;
import com.br.oficina.interfaces.rest.dto.VeiculoRequest;
import com.br.oficina.interfaces.rest.dto.VeiculoResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VeiculoMapper {
    Veiculo toEntity(VeiculoRequest request);
    VeiculoResponse toResponse(Veiculo veiculo);
    List<VeiculoResponse> toResponseList(List<Veiculo> veiculos);
}
