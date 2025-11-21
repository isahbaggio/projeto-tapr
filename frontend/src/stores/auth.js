import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authService } from '@/services/auth.service'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const accessToken = ref(null)
  const loading = ref(false)
  const error = ref(null)

  const isAuthenticated = computed(() => !!accessToken.value)
  const userRole = computed(() => user.value?.role)

  function loadFromStorage() {
    const token = localStorage.getItem('accessToken')
    const userData = localStorage.getItem('user')

    if (token && userData) {
      accessToken.value = token
      user.value = JSON.parse(userData)
    }
  }

  async function login(email, password) {
    try {
      loading.value = true
      error.value = null

      const response = await authService.login(email, password)

      accessToken.value = response.accessToken

      const tokenParts = response.accessToken.split('.')
      const payload = JSON.parse(atob(tokenParts[1]))

      user.value = {
        id: payload.sub,
        email: payload.email,
        role: payload.role
      }

      localStorage.setItem('accessToken', response.accessToken)
      localStorage.setItem('user', JSON.stringify(user.value))

      return true
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao fazer login'
      throw err
    } finally {
      loading.value = false
    }
  }

  function logout() {
    authService.logout()
    user.value = null
    accessToken.value = null
  }

  return {
    user,
    accessToken,
    loading,
    error,
    isAuthenticated,
    userRole,
    loadFromStorage,
    login,
    logout
  }
})
