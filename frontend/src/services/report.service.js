import api from './api'

export default {
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
  }
}
