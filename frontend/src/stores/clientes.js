import { defineStore } from 'pinia'
import { ref } from 'vue'
import { clienteService } from '@/services/cliente.service'

export const useClientesStore = defineStore('clientes', () => {
  const clientes = ref([])
  const loading = ref(false)
  const error = ref(null)

  async function fetchClientes() {
    try {
      loading.value = true
      error.value = null
      clientes.value = await clienteService.getAll()
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao carregar clientes'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function createCliente(cliente) {
    try {
      loading.value = true
      error.value = null
      const created = await clienteService.create(cliente)
      clientes.value.push(created)
      return created
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao criar cliente'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function updateCliente(id, cliente) {
    try {
      loading.value = true
      error.value = null
      const updated = await clienteService.update(id, cliente)
      const index = clientes.value.findIndex(c => c.id === id)
      if (index !== -1) {
        clientes.value[index] = updated
      }
      return updated
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao atualizar cliente'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function deleteCliente(id) {
    try {
      loading.value = true
      error.value = null
      await clienteService.delete(id)
      clientes.value = clientes.value.filter(c => c.id !== id)
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao deletar cliente'
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    clientes,
    loading,
    error,
    fetchClientes,
    createCliente,
    updateCliente,
    deleteCliente
  }
})
