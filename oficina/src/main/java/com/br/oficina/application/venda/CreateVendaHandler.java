package com.br.oficina.application.venda;

import com.br.oficina.domain.venda.VendaRepository;
import com.br.oficina.domain.venda.Venda;
import com.br.oficina.domain.venda.ItemVenda;
import com.br.oficina.domain.venda.vo.TipoItem;
import com.br.oficina.domain.cliente.ClienteRepository;
import com.br.oficina.domain.produto.ProdutoRepository;
import com.br.oficina.domain.servico.ServicoRepository;
import com.br.oficina.infrastructure.exception.EntidadeNaoEncontradaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateVendaHandler {

    private final VendaRepository vendaRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ServicoRepository servicoRepository;

    @Transactional
    public Venda handle(Venda venda) {
        if (!clienteRepository.existsById(venda.getClienteId())) {
            throw new EntidadeNaoEncontradaException("Cliente não encontrado");
        }

        for (ItemVenda item : venda.getItens()) {
            if (item.getTipoItem() == TipoItem.PRODUTO) {
                if (!produtoRepository.existsById(item.getItemId())) {
                    throw new EntidadeNaoEncontradaException("Produto não encontrado: " + item.getItemId());
                }
            } else if (item.getTipoItem() == TipoItem.SERVICO) {
                if (!servicoRepository.existsById(item.getItemId())) {
                    throw new EntidadeNaoEncontradaException("Serviço não encontrado: " + item.getItemId());
                }
            }
            item.setVenda(venda);
            item.calcularSubtotal();
        }

        venda.calcularValorTotal();
        return vendaRepository.save(venda);
    }
}
