import api from './api'

export const servicoService = {
  async getAll() {
    const response = await api.get('/oficina/servicos')
    return response.data
  },

  async getById(id) {
    const response = await api.get(`/oficina/servicos/${id}`)
    return response.data
  },

  async create(servico) {
    const response = await api.post('/oficina/servicos', servico)
    return response.data
  },

  async update(id, servico) {
    const response = await api.put(`/oficina/servicos/${id}`, servico)
    return response.data
  },

  async delete(id) {
    await api.delete(`/oficina/servicos/${id}`)
  }
}
