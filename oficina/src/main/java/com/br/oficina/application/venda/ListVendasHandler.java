package com.br.oficina.application.venda;

import com.br.oficina.domain.venda.VendaRepository;
import com.br.oficina.domain.venda.Venda;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListVendasHandler {

    private final VendaRepository vendaRepository;

    public List<Venda> handle() {
        return vendaRepository.findAll();
    }
}
