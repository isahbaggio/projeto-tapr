package com.br.oficina.interfaces.rest;

import com.br.oficina.application.relatorio.*;
import com.br.oficina.interfaces.rest.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioVendasPorPeriodoHandler relatorioVendasPorPeriodoHandler;
    private final RelatorioProdutosMaisVendidosHandler relatorioProdutosMaisVendidosHandler;
    private final RelatorioServicosMaisVendidosHandler relatorioServicosMaisVendidosHandler;
    private final RelatorioVendasPorClienteHandler relatorioVendasPorClienteHandler;
    private final RelatorioEstatisticasProdutosHandler relatorioEstatisticasProdutosHandler;
    private final RelatorioEstatisticasServicosHandler relatorioEstatisticasServicosHandler;
    private final RelatorioEstatisticasClientesHandler relatorioEstatisticasClientesHandler;
    private final RelatorioSistemaHandler relatorioSistemaHandler;

    @GetMapping("/vendas-por-periodo")
    public ResponseEntity<RelatorioVendasPorPeriodoResponse> vendasPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        RelatorioVendasPorPeriodoResponse relatorio = relatorioVendasPorPeriodoHandler.handle(inicio, fim);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/produtos-mais-vendidos")
    public ResponseEntity<List<RelatorioItemMaisVendidoResponse>> produtosMaisVendidos() {
        List<RelatorioItemMaisVendidoResponse> relatorio = relatorioProdutosMaisVendidosHandler.handle();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/servicos-mais-vendidos")
    public ResponseEntity<List<RelatorioItemMaisVendidoResponse>> servicosMaisVendidos() {
        List<RelatorioItemMaisVendidoResponse> relatorio = relatorioServicosMaisVendidosHandler.handle();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/vendas-por-cliente")
    public ResponseEntity<List<RelatorioVendasPorClienteResponse>> vendasPorCliente() {
        List<RelatorioVendasPorClienteResponse> relatorio = relatorioVendasPorClienteHandler.handle();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/produtos")
    public ResponseEntity<RelatorioEstatisticasProdutosResponse> estatisticasProdutos() {
        RelatorioEstatisticasProdutosResponse relatorio = relatorioEstatisticasProdutosHandler.handle();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/servicos")
    public ResponseEntity<RelatorioEstatisticasServicosResponse> estatisticasServicos() {
        RelatorioEstatisticasServicosResponse relatorio = relatorioEstatisticasServicosHandler.handle();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/clientes")
    public ResponseEntity<RelatorioEstatisticasClientesResponse> estatisticasClientes() {
        RelatorioEstatisticasClientesResponse relatorio = relatorioEstatisticasClientesHandler.handle();
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/sistema")
    public ResponseEntity<RelatorioSistemaResponse> visaoGeralSistema() {
        RelatorioSistemaResponse relatorio = relatorioSistemaHandler.handle();
        return ResponseEntity.ok(relatorio);
    }
}
