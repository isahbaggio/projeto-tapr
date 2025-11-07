package com.br.oficina.interfaces.rest.mapper;

import com.br.oficina.domain.venda.Venda;
import com.br.oficina.domain.venda.ItemVenda;
import com.br.oficina.interfaces.rest.dto.VendaRequest;
import com.br.oficina.interfaces.rest.dto.VendaResponse;
import com.br.oficina.interfaces.rest.dto.ItemVendaRequest;
import com.br.oficina.interfaces.rest.dto.ItemVendaResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VendaMapper {
    Venda toEntity(VendaRequest request);
    VendaResponse toResponse(Venda venda);
    List<VendaResponse> toResponseList(List<Venda> vendas);

    ItemVenda toItemEntity(ItemVendaRequest request);
    ItemVendaResponse toItemResponse(ItemVenda item);
}
