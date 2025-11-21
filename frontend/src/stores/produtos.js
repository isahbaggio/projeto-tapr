import { defineStore } from 'pinia'
import { ref } from 'vue'
import { produtoService } from '@/services/produto.service'

export const useProdutosStore = defineStore('produtos', () => {
  const produtos = ref([])
  const loading = ref(false)
  const error = ref(null)

  async function fetchProdutos() {
    try {
      loading.value = true
      error.value = null
      produtos.value = await produtoService.getAll()
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao carregar produtos'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function createProduto(produto) {
    try {
      loading.value = true
      error.value = null
      const created = await produtoService.create(produto)
      produtos.value.push(created)
      return created
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao criar produto'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function updateProduto(id, produto) {
    try {
      loading.value = true
      error.value = null
      const updated = await produtoService.update(id, produto)
      const index = produtos.value.findIndex(p => p.id === id)
      if (index !== -1) {
        produtos.value[index] = updated
      }
      return updated
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao atualizar produto'
      throw err
    } finally {
      loading.value = false
    }
  }

  async function deleteProduto(id) {
    try {
      loading.value = true
      error.value = null
      await produtoService.delete(id)
      produtos.value = produtos.value.filter(p => p.id !== id)
    } catch (err) {
      error.value = err.response?.data?.message || 'Erro ao deletar produto'
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    produtos,
    loading,
    error,
    fetchProdutos,
    createProduto,
    updateProduto,
    deleteProduto
  }
})
