import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { vendaService } from '@/services/venda.service'
import { useNotificationStore } from './notification'

export const useVendasStore = defineStore('vendas', () => {
  const vendas = ref([])
  const loading = ref(false)
  const error = ref(null)

  const vendasAtivas = computed(() =>
    vendas.value.filter(v => !v.cancelada)
  )

  const vendasCanceladas = computed(() =>
    vendas.value.filter(v => v.cancelada)
  )

  async function fetchVendas() {
    loading.value = true
    error.value = null
    try {
      vendas.value = await vendaService.getAll()
    } catch (err) {
      error.value = err.message
      useNotificationStore().error('Erro ao carregar vendas')
    } finally {
      loading.value = false
    }
  }

  async function fetchVendasByCliente(clienteId) {
    loading.value = true
    error.value = null
    try {
      vendas.value = await vendaService.getByClienteId(clienteId)
    } catch (err) {
      error.value = err.message
      useNotificationStore().error('Erro ao carregar vendas do cliente')
    } finally {
      loading.value = false
    }
  }

  async function getVendaById(id) {
    try {
      return await vendaService.getById(id)
    } catch (err) {
      useNotificationStore().error('Erro ao buscar venda')
      throw err
    }
  }

  async function createVenda(venda) {
    loading.value = true
    error.value = null
    try {
      const novaVenda = await vendaService.create(venda)
      vendas.value.unshift(novaVenda)
      useNotificationStore().success('Venda criada com sucesso!')
      return novaVenda
    } catch (err) {
      error.value = err.message
      useNotificationStore().error('Erro ao criar venda')
      throw err
    } finally {
      loading.value = false
    }
  }

  async function cancelarVenda(id) {
    try {
      await vendaService.cancelar(id)
      const venda = vendas.value.find(v => v.id === id)
      if (venda) {
        venda.cancelada = true
      }
      useNotificationStore().success('Venda cancelada com sucesso!')
    } catch (err) {
      useNotificationStore().error('Erro ao cancelar venda')
      throw err
    }
  }

  function clearVendas() {
    vendas.value = []
  }

  return {
    vendas,
    vendasAtivas,
    vendasCanceladas,
    loading,
    error,
    fetchVendas,
    fetchVendasByCliente,
    getVendaById,
    createVenda,
    cancelarVenda,
    clearVendas
  }
})
