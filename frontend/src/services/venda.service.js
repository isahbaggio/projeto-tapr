import api from './api'

export const vendaService = {
  async getAll() {
    const response = await api.get('/oficina/vendas')
    return response.data
  },

  async getById(id) {
    const response = await api.get(`/oficina/vendas/${id}`)
    return response.data
  },

  async getByClienteId(clienteId) {
    const response = await api.get(`/oficina/vendas/cliente/${clienteId}`)
    return response.data
  },

  async create(venda) {
    const response = await api.post('/oficina/vendas', venda)
    return response.data
  },

  async cancelar(id) {
    await api.delete(`/oficina/vendas/${id}`)
  }
}

export default vendaService
