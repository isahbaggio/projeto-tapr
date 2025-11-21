import api from './api'

export const authService = {
  async login(email, password) {
    const response = await api.post('/auth-service/auth/login/password', {
      email,
      password
    })
    return response.data
  },

  async requestMagicLink(email) {
    const response = await api.post('/auth-service/auth/login/magic', {
      email
    })
    return response.data
  },

  async verifyMagicLink(token) {
    const response = await api.post('/auth-service/auth/login/magic/verify', null, {
      params: { token }
    })
    return response.data
  },

  logout() {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('user')
  }
}
