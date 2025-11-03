package com.br.oficina.interfaces.rest.mapper;

import com.br.oficina.domain.produto.Produto;
import com.br.oficina.interfaces.rest.dto.ProdutoRequest;
import com.br.oficina.interfaces.rest.dto.ProdutoResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    Produto toEntity(ProdutoRequest request);
    ProdutoResponse toResponse(Produto produto);
    List<ProdutoResponse> toResponseList(List<Produto> produtos);
}
