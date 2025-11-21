import api from './api'

export const reportService = {
  getProdutoReport() {
    return api.get('/oficina/relatorios/produtos')
  },

  getServicoReport() {
    return api.get('/oficina/relatorios/servicos')
  },

  getClienteReport() {
    return api.get('/oficina/relatorios/clientes')
  },

  getSystemOverview() {
    return api.get('/oficina/relatorios/sistema')
  },

  // Rankings
  getProdutosMaisVendidos() {
    return api.get('/oficina/relatorios/produtos-mais-vendidos')
  },

  getServicosMaisVendidos() {
    return api.get('/oficina/relatorios/servicos-mais-vendidos')
  },

  getVendasPorCliente() {
    return api.get('/oficina/relatorios/vendas-por-cliente')
  }
}

export default reportService
