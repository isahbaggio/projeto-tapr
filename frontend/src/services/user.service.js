import api from './api'

export const userService = {
  async getAll(page = 0, size = 10) {
    const response = await api.get('/auth-service/auth/users', {
      params: { page, size }
    })
    return response.data
  },

  async getById(id) {
    const response = await api.get(`/auth-service/auth/users/${id}`)
    return response.data
  },

  async create(userData) {
    const response = await api.post('/auth-service/auth/users', userData)
    return response.data
  },

  async update(id, userData) {
    const response = await api.put(`/auth-service/auth/users/${id}`, userData)
    return response.data
  },

  async changeRole(id, role) {
    const response = await api.patch(`/auth-service/auth/users/${id}/role`, { role })
    return response.data
  }
}
