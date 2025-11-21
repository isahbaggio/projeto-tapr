import api from './api'

export const produtoService = {
  async getAll() {
    const response = await api.get('/oficina/produtos')
    return response.data
  },

  async getById(id) {
    const response = await api.get(`/oficina/produtos/${id}`)
    return response.data
  },

  async create(produto) {
    const response = await api.post('/oficina/produtos', produto)
    return response.data
  },

  async update(id, produto) {
    const response = await api.put(`/oficina/produtos/${id}`, produto)
    return response.data
  },

  async delete(id) {
    await api.delete(`/oficina/produtos/${id}`)
  }
}
