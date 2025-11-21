<template>
  <div class="p-6">
    <div class="mb-6 flex justify-between items-center">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Vendas</h1>
        <p class="text-gray-600 mt-1">Gerencie as vendas da oficina</p>
      </div>
      <div class="flex space-x-2">
        <button
          @click="exportarCSV"
          class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors"
        >
          Exportar CSV
        </button>
        <button
          @click="$router.push('/vendas/nova')"
          class="px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 transition-colors"
        >
          Nova Venda
        </button>
      </div>
    </div>

    <!-- Filtros -->
    <div class="mb-6">
      <button
        @click="mostrarFiltros = !mostrarFiltros"
        class="flex items-center text-primary-600 hover:text-primary-700 mb-4"
      >
        {{ mostrarFiltros ? '▼' : '▶' }} Filtros Avançados
      </button>
      <VendaFilters
        v-if="mostrarFiltros"
        :clientes="clientes"
        :filters="filters"
        @apply-filters="aplicarFiltros"
        @clear-filters="limparFiltros"
      />
    </div>

    <!-- Loading -->
    <div v-if="vendasStore.loading" class="flex justify-center items-center h-64">
      <LoadingSpinner />
    </div>

    <!-- Lista de Vendas -->
    <div v-else-if="vendasFiltradas.length > 0" class="bg-white shadow-md rounded-lg overflow-hidden">
      <table class="min-w-full divide-y divide-gray-200">
        <thead class="bg-gray-50">
          <tr>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Data
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Cliente
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Itens
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Valor Total
            </th>
            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
              Status
            </th>
            <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
              Ações
            </th>
          </tr>
        </thead>
        <tbody class="bg-white divide-y divide-gray-200">
          <tr v-for="venda in vendasFiltradas" :key="venda.id" class="hover:bg-gray-50">
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
              {{ formatDate(venda.dataVenda) }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
              {{ getClienteNome(venda.clienteId) }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
              {{ venda.itens.length }} item(ns)
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
              R$ {{ formatCurrency(venda.valorTotal) }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              <span
                :class="[
                  'px-2 inline-flex text-xs leading-5 font-semibold rounded-full',
                  venda.cancelada
                    ? 'bg-red-100 text-red-800'
                    : 'bg-green-100 text-green-800'
                ]"
              >
                {{ venda.cancelada ? 'Cancelada' : 'Ativa' }}
              </span>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium space-x-2">
              <button
                @click="visualizarVenda(venda)"
                class="text-primary-600 hover:text-primary-900"
              >
                Ver Detalhes
              </button>
              <button
                @click="imprimirRecibo(venda)"
                class="text-blue-600 hover:text-blue-900"
              >
                Imprimir
              </button>
              <button
                v-if="!venda.cancelada"
                @click="confirmarCancelamento(venda)"
                class="text-red-600 hover:text-red-900"
              >
                Cancelar
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Empty State -->
    <div v-else class="bg-white shadow-md rounded-lg p-12">
      <EmptyState message="Nenhuma venda encontrada" />
    </div>

    <!-- Modal de Detalhes -->
    <VendaDetalhesModal
      :show="showDetalhesModal"
      :venda="vendaSelecionada"
      @close="showDetalhesModal = false"
      @cancelar="cancelarVenda"
    />

    <!-- Confirm Dialog -->
    <ConfirmDialog
      :show="showConfirmDialog"
      title="Cancelar Venda"
      message="Tem certeza que deseja cancelar esta venda? Esta ação não pode ser desfeita."
      @confirm="executarCancelamento"
      @cancel="showConfirmDialog = false"
    />
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useVendasStore } from '@/stores/vendas'
import { useClientesStore } from '@/stores/clientes'
import VendaFilters from '@/components/vendas/VendaFilters.vue'
import VendaDetalhesModal from '@/components/vendas/VendaDetalhesModal.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import EmptyState from '@/components/common/EmptyState.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'
import { formatDate, formatCurrency } from '@/utils/formatters'
import { gerarReciboPDF, exportarVendasCSV } from '@/utils/pdfGenerator'

export default {
  name: 'VendasView',
  components: {
    VendaFilters,
    VendaDetalhesModal,
    LoadingSpinner,
    EmptyState,
    ConfirmDialog
  },
  setup() {
    const vendasStore = useVendasStore()
    const clientesStore = useClientesStore()

    const mostrarFiltros = ref(false)
    const showDetalhesModal = ref(false)
    const showConfirmDialog = ref(false)
    const vendaSelecionada = ref(null)

    const filters = ref({
      clienteId: '',
      dataInicio: '',
      dataFim: '',
      status: 'todas',
      valorMinimo: null,
      valorMaximo: null
    })

    const clientes = computed(() => clientesStore.clientes)

    const vendasFiltradas = computed(() => {
      let vendas = [...vendasStore.vendas]

      // Filtro por cliente
      if (filters.value.clienteId) {
        vendas = vendas.filter(v => v.clienteId === filters.value.clienteId)
      }

      // Filtro por data
      if (filters.value.dataInicio) {
        const dataInicio = new Date(filters.value.dataInicio)
        vendas = vendas.filter(v => new Date(v.dataVenda) >= dataInicio)
      }
      if (filters.value.dataFim) {
        const dataFim = new Date(filters.value.dataFim)
        dataFim.setHours(23, 59, 59)
        vendas = vendas.filter(v => new Date(v.dataVenda) <= dataFim)
      }

      // Filtro por status
      if (filters.value.status === 'ativas') {
        vendas = vendas.filter(v => !v.cancelada)
      } else if (filters.value.status === 'canceladas') {
        vendas = vendas.filter(v => v.cancelada)
      }

      // Filtro por valor
      if (filters.value.valorMinimo !== null && filters.value.valorMinimo > 0) {
        vendas = vendas.filter(v => v.valorTotal >= filters.value.valorMinimo)
      }
      if (filters.value.valorMaximo !== null && filters.value.valorMaximo > 0) {
        vendas = vendas.filter(v => v.valorTotal <= filters.value.valorMaximo)
      }

      // Ordenar por data (mais recente primeiro)
      return vendas.sort((a, b) => new Date(b.dataVenda) - new Date(a.dataVenda))
    })

    onMounted(async () => {
      await Promise.all([
        vendasStore.fetchVendas(),
        clientesStore.fetchClientes()
      ])
    })

    function aplicarFiltros(newFilters) {
      filters.value = { ...newFilters }
    }

    function limparFiltros() {
      filters.value = {
        clienteId: '',
        dataInicio: '',
        dataFim: '',
        status: 'todas',
        valorMinimo: null,
        valorMaximo: null
      }
    }

    function getClienteNome(clienteId) {
      const cliente = clientes.value.find(c => c.id === clienteId)
      return cliente ? cliente.nome : 'Cliente não encontrado'
    }

    function visualizarVenda(venda) {
      vendaSelecionada.value = venda
      showDetalhesModal.value = true
    }

    function imprimirRecibo(venda) {
      const cliente = clientes.value.find(c => c.id === venda.clienteId)
      gerarReciboPDF(venda, cliente)
    }

    function confirmarCancelamento(venda) {
      vendaSelecionada.value = venda
      showConfirmDialog.value = true
    }

    async function executarCancelamento() {
      if (vendaSelecionada.value) {
        await cancelarVenda(vendaSelecionada.value.id)
      }
      showConfirmDialog.value = false
    }

    async function cancelarVenda(vendaId) {
      try {
        await vendasStore.cancelarVenda(vendaId)
        showDetalhesModal.value = false
      } catch (error) {
        console.error('Erro ao cancelar venda:', error)
      }
    }

    function exportarCSV() {
      exportarVendasCSV(vendasFiltradas.value)
    }

    return {
      vendasStore,
      mostrarFiltros,
      showDetalhesModal,
      showConfirmDialog,
      vendaSelecionada,
      filters,
      clientes,
      vendasFiltradas,
      aplicarFiltros,
      limparFiltros,
      getClienteNome,
      visualizarVenda,
      imprimirRecibo,
      confirmarCancelamento,
      executarCancelamento,
      cancelarVenda,
      exportarCSV,
      formatDate,
      formatCurrency
    }
  }
}
</script>
