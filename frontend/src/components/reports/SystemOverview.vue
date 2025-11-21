<script setup>
import { computed } from 'vue'
import BarChart from '../charts/BarChart.vue'
import { formatCurrency } from '@/utils/formatters'

const props = defineProps({
  overview: {
    type: Object,
    required: true
  }
})

const revenueChartData = computed(() => {
  return {
    labels: ['Produtos', 'Servicos'],
    datasets: [{
      label: 'Receita Potencial',
      data: [props.overview.receitaPotencialProdutos, props.overview.receitaPotencialServicos],
      backgroundColor: ['#3B82F6', '#10B981']
    }]
  }
})
</script>

<template>
  <div class="space-y-6">
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <div class="bg-gradient-to-br from-blue-500 to-blue-600 rounded-lg shadow-lg p-6 text-white">
        <div class="text-sm opacity-90">Total de Produtos</div>
        <div class="text-3xl font-bold mt-2">{{ overview.totalProdutos }}</div>
      </div>
      <div class="bg-gradient-to-br from-green-500 to-green-600 rounded-lg shadow-lg p-6 text-white">
        <div class="text-sm opacity-90">Total de Servicos</div>
        <div class="text-3xl font-bold mt-2">{{ overview.totalServicos }}</div>
      </div>
      <div class="bg-gradient-to-br from-purple-500 to-purple-600 rounded-lg shadow-lg p-6 text-white">
        <div class="text-sm opacity-90">Total de Clientes</div>
        <div class="text-3xl font-bold mt-2">{{ overview.totalClientes }}</div>
      </div>
      <div class="bg-gradient-to-br from-yellow-500 to-yellow-600 rounded-lg shadow-lg p-6 text-white">
        <div class="text-sm opacity-90">Receita Potencial Total</div>
        <div class="text-2xl font-bold mt-2">{{ formatCurrency(overview.receitaPotencialTotal) }}</div>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow p-6">
      <h3 class="text-lg font-semibold text-gray-900 mb-4">Receita Potencial por Tipo</h3>
      <div class="h-80">
        <BarChart :data="revenueChartData" />
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div class="bg-white rounded-lg shadow p-6">
        <h4 class="text-md font-semibold text-gray-900 mb-3">Receita de Produtos</h4>
        <div class="text-3xl font-bold text-blue-600">{{ formatCurrency(overview.receitaPotencialProdutos) }}</div>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <h4 class="text-md font-semibold text-gray-900 mb-3">Receita de Servicos</h4>
        <div class="text-3xl font-bold text-green-600">{{ formatCurrency(overview.receitaPotencialServicos) }}</div>
      </div>
    </div>
  </div>
</template>
