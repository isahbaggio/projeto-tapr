import { defineStore } from 'pinia'
import { ref } from 'vue'
import { servicoService } from '@/services/servico.service'

export const useServicosStore = defineStore('servicos', () => {
  const servicos = ref([])
  const loading = ref(false)
  const error = ref(null)

  async function fetchServicos() {
    try {
      loading.value = true
      error.value = null
      servicos.value = await servicoService.getAll()
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao carregar serviços'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function createServico(servico) {
    try {
      loading.value = true
      error.value = null
      const created = await servicoService.create(servico)
      servicos.value.push(created)
      return created
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao criar serviço'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function updateServico(id, servico) {
    try {
      loading.value = true
      error.value = null
      const updated = await servicoService.update(id, servico)
      const index = servicos.value.findIndex(s => s.id === id)
      if (index !== -1) {
        servicos.value[index] = updated
      }
      return updated
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao atualizar serviço'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function deleteServico(id) {
    try {
      loading.value = true
      error.value = null
      await servicoService.delete(id)
      servicos.value = servicos.value.filter(s => s.id !== id)
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao deletar serviço'
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    servicos,
    loading,
    error,
    fetchServicos,
    createServico,
    updateServico,
    deleteServico
  }
})
