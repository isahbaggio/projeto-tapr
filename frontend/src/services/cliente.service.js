import api from './api'

export const clienteService = {
  async getAll() {
    const response = await api.get('/oficina/clientes')
    return response.data
  },

  async getById(id) {
    const response = await api.get(`/oficina/clientes/${id}`)
    return response.data
  },

  async create(cliente) {
    const response = await api.post('/oficina/clientes', cliente)
    return response.data
  },

  async update(id, cliente) {
    const response = await api.put(`/oficina/clientes/${id}`, cliente)
    return response.data
  },

  async delete(id) {
    await api.delete(`/oficina/clientes/${id}`)
  }
}
