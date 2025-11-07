package com.br.oficina.infrastructure.persistence;

import com.br.oficina.domain.venda.ItemVenda;
import com.br.oficina.domain.venda.vo.TipoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SpringDataItemVendaJpa extends JpaRepository<ItemVenda, UUID> {

    @Query("SELECT i.itemId, i.itemNome, i.tipoItem, SUM(i.quantidade) as quantidadeVendida, SUM(i.subtotal) as valorTotal " +
           "FROM ItemVenda i " +
           "JOIN i.venda v " +
           "WHERE v.cancelada = false AND i.tipoItem = :tipoItem " +
           "GROUP BY i.itemId, i.itemNome, i.tipoItem " +
           "ORDER BY quantidadeVendida DESC")
    List<Object[]> findMaisVendidosByTipo(@Param("tipoItem") TipoItem tipoItem);

    @Query("SELECT v.clienteId, SUM(i.subtotal) as valorTotal, COUNT(DISTINCT v.id) as totalVendas " +
           "FROM ItemVenda i " +
           "JOIN i.venda v " +
           "WHERE v.cancelada = false " +
           "GROUP BY v.clienteId " +
           "ORDER BY valorTotal DESC")
    List<Object[]> findVendasPorCliente();
}
