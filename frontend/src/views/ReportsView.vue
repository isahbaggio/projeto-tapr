<script setup>
import { ref, onMounted } from 'vue'
import { useReportStore } from '@/stores/report'
import ProdutoReports from '@/components/reports/ProdutoReports.vue'
import ServicoReports from '@/components/reports/ServicoReports.vue'
import ClienteReports from '@/components/reports/ClienteReports.vue'
import SystemOverview from '@/components/reports/SystemOverview.vue'
import { exportToCSV } from '@/utils/exportUtils'

const reportStore = useReportStore()
const activeTab = ref('overview')

const tabs = [
  { id: 'overview', name: 'Visao Geral' },
  { id: 'produtos', name: 'Produtos' },
  { id: 'servicos', name: 'Servicos' },
  { id: 'clientes', name: 'Clientes' }
]

onMounted(() => {
  reportStore.fetchAllReports()
})

function handleExport(type) {
  let data = []
  let filename = ''

  switch (activeTab.value) {
    case 'overview':
      data = [{
        'Total Produtos': reportStore.systemOverview?.totalProdutos,
        'Total Servicos': reportStore.systemOverview?.totalServicos,
        'Total Clientes': reportStore.systemOverview?.totalClientes,
        'Receita Potencial': reportStore.systemOverview?.receitaPotencialTotal
      }]
      filename = 'visao-geral'
      break
    case 'produtos':
      data = [{
        'Total': reportStore.produtoReport?.totalProdutos,
        'Ativos': reportStore.produtoReport?.produtosAtivos,
        'Inativos': reportStore.produtoReport?.produtosInativos,
        'Preco Medio': reportStore.produtoReport?.precoMedio
      }]
      filename = 'relatorio-produtos'
      break
    case 'servicos':
      data = [{
        'Total': reportStore.servicoReport?.totalServicos,
        'Ativos': reportStore.servicoReport?.servicosAtivos,
        'Inativos': reportStore.servicoReport?.servicosInativos,
        'Preco Medio': reportStore.servicoReport?.precoMedio,
        'Duracao Media': reportStore.servicoReport?.duracaoMedia
      }]
      filename = 'relatorio-servicos'
      break
    case 'clientes':
      data = [{
        'Total': reportStore.clienteReport?.totalClientes,
        'Ativos': reportStore.clienteReport?.clientesAtivos,
        'Inativos': reportStore.clienteReport?.clientesInativos
      }]
      filename = 'relatorio-clientes'
      break
  }

  exportToCSV(data, filename)
}
</script>

<template>
  <div class="p-6">
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-900">Relatorios</h1>
      <p class="text-gray-600 mt-1">Visualize estatisticas e metricas do sistema</p>
    </div>

    <div class="mb-6 flex justify-between items-center">
      <div class="border-b border-gray-200">
        <nav class="-mb-px flex space-x-8">
          <button
            v-for="tab in tabs"
            :key="tab.id"
            @click="activeTab = tab.id"
            :class="[
              'whitespace-nowrap py-4 px-1 border-b-2 font-medium text-sm',
              activeTab === tab.id
                ? 'border-primary-500 text-primary-600'
                : 'border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300'
            ]"
          >
            {{ tab.name }}
          </button>
        </nav>
      </div>

      <button
        @click="handleExport"
        class="px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 transition-colors"
      >
        Exportar CSV
      </button>
    </div>

    <div v-if="reportStore.loading" class="flex justify-center items-center h-64">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div>
    </div>

    <div v-else>
      <SystemOverview
        v-if="activeTab === 'overview' && reportStore.systemOverview"
        :overview="reportStore.systemOverview"
      />
      <ProdutoReports
        v-if="activeTab === 'produtos' && reportStore.produtoReport"
        :report="reportStore.produtoReport"
      />
      <ServicoReports
        v-if="activeTab === 'servicos' && reportStore.servicoReport"
        :report="reportStore.servicoReport"
      />
      <ClienteReports
        v-if="activeTab === 'clientes' && reportStore.clienteReport"
        :report="reportStore.clienteReport"
      />
    </div>
  </div>
</template>
