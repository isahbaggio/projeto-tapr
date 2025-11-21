<template>
  <div class="space-y-6">
    <div>
      <h2 class="text-2xl font-bold text-gray-900 mb-6">Rankings de Vendas</h2>
    </div>

    <!-- Produtos Mais Vendidos -->
    <div class="bg-white shadow-md rounded-lg p-6">
      <h3 class="text-lg font-semibold text-gray-900 mb-4">Produtos Mais Vendidos</h3>
      <div v-if="loadingProdutos" class="flex justify-center py-8">
        <LoadingSpinner />
      </div>
      <div v-else-if="produtosMaisVendidos.length === 0" class="text-center py-8">
        <EmptyState message="Nenhum produto vendido ainda" />
      </div>
      <div v-else class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Posição
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Produto
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Quantidade Vendida
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Valor Total
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="(produto, index) in produtosMaisVendidos" :key="produto.itemId">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                {{ index + 1 }}º
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                {{ produto.itemNome }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                {{ produto.quantidadeVendida }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                {{ formatCurrency(produto.valorTotal) }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Serviços Mais Vendidos -->
    <div class="bg-white shadow-md rounded-lg p-6">
      <h3 class="text-lg font-semibold text-gray-900 mb-4">Serviços Mais Vendidos</h3>
      <div v-if="loadingServicos" class="flex justify-center py-8">
        <LoadingSpinner />
      </div>
      <div v-else-if="servicosMaisVendidos.length === 0" class="text-center py-8">
        <EmptyState message="Nenhum serviço vendido ainda" />
      </div>
      <div v-else class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Posição
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Serviço
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Quantidade Vendida
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Valor Total
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="(servico, index) in servicosMaisVendidos" :key="servico.itemId">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                {{ index + 1 }}º
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                {{ servico.itemNome }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                {{ servico.quantidadeVendida }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                {{ formatCurrency(servico.valorTotal) }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Clientes com Mais Compras -->
    <div class="bg-white shadow-md rounded-lg p-6">
      <h3 class="text-lg font-semibold text-gray-900 mb-4">Clientes com Mais Compras</h3>
      <div v-if="loadingClientes" class="flex justify-center py-8">
        <LoadingSpinner />
      </div>
      <div v-else-if="clientesMaisCompras.length === 0" class="text-center py-8">
        <EmptyState message="Nenhuma compra registrada ainda" />
      </div>
      <div v-else class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Posição
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Cliente
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Total de Vendas
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Valor Total
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="(cliente, index) in clientesMaisCompras" :key="cliente.clienteId">
              <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                {{ index + 1 }}º
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                {{ cliente.clienteNome }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                {{ cliente.totalVendas }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
                {{ formatCurrency(cliente.valorTotal) }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import LoadingSpinner from '../ui/LoadingSpinner.vue'
import EmptyState from '../common/EmptyState.vue'
import { reportService } from '@/services/report.service'
import { formatCurrency } from '@/utils/formatters'

export default {
  name: 'RankingsReports',
  components: {
    LoadingSpinner,
    EmptyState
  },
  setup() {
    const produtosMaisVendidos = ref([])
    const servicosMaisVendidos = ref([])
    const clientesMaisCompras = ref([])
    const loadingProdutos = ref(false)
    const loadingServicos = ref(false)
    const loadingClientes = ref(false)

    const loadProdutosMaisVendidos = async () => {
      loadingProdutos.value = true
      try {
        const response = await reportService.getProdutosMaisVendidos()
        produtosMaisVendidos.value = response.data
      } catch (error) {
        console.error('Erro ao carregar produtos mais vendidos:', error)
      } finally {
        loadingProdutos.value = false
      }
    }

    const loadServicosMaisVendidos = async () => {
      loadingServicos.value = true
      try {
        const response = await reportService.getServicosMaisVendidos()
        servicosMaisVendidos.value = response.data
      } catch (error) {
        console.error('Erro ao carregar serviços mais vendidos:', error)
      } finally {
        loadingServicos.value = false
      }
    }

    const loadClientesMaisCompras = async () => {
      loadingClientes.value = true
      try {
        const response = await reportService.getVendasPorCliente()
        clientesMaisCompras.value = response.data
      } catch (error) {
        console.error('Erro ao carregar clientes com mais compras:', error)
      } finally {
        loadingClientes.value = false
      }
    }

    onMounted(() => {
      loadProdutosMaisVendidos()
      loadServicosMaisVendidos()
      loadClientesMaisCompras()
    })

    return {
      produtosMaisVendidos,
      servicosMaisVendidos,
      clientesMaisCompras,
      loadingProdutos,
      loadingServicos,
      loadingClientes,
      formatCurrency
    }
  }
}
</script>
