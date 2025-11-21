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
  }
}
