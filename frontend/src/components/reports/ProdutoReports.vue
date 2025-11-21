<script setup>
import { computed } from 'vue'
import BarChart from '../charts/BarChart.vue'
import PieChart from '../charts/PieChart.vue'
import { formatCurrency } from '@/utils/formatters'

const props = defineProps({
  report: {
    type: Object,
    required: true
  }
})

const categoriesChartData = computed(() => {
  const categories = props.report.produtosPorCategoria || {}
  return {
    labels: Object.keys(categories),
    datasets: [{
      label: 'Produtos por Categoria',
      data: Object.values(categories),
      backgroundColor: [
        '#3B82F6',
        '#10B981',
        '#F59E0B',
        '#EF4444',
        '#8B5CF6',
        '#EC4899'
      ]
    }]
  }
})

const activeStatusChartData = computed(() => {
  return {
    labels: ['Ativos', 'Inativos'],
    datasets: [{
      data: [props.report.produtosAtivos, props.report.produtosInativos],
      backgroundColor: ['#10B981', '#EF4444']
    }]
  }
})
</script>

<template>
  <div class="space-y-6">
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
      <div class="bg-white rounded-lg shadow p-6">
        <div class="text-sm text-gray-600">Total de Produtos</div>
        <div class="text-2xl font-bold text-gray-900 mt-2">{{ report.totalProdutos }}</div>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <div class="text-sm text-gray-600">Preco Medio</div>
        <div class="text-2xl font-bold text-primary-600 mt-2">{{ formatCurrency(report.precoMedio) }}</div>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <div class="text-sm text-gray-600">Preco Mais Alto</div>
        <div class="text-2xl font-bold text-green-600 mt-2">{{ formatCurrency(report.precoMaisAlto) }}</div>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <div class="text-sm text-gray-600">Preco Mais Baixo</div>
        <div class="text-2xl font-bold text-gray-600 mt-2">{{ formatCurrency(report.precoMaisBaixo) }}</div>
      </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
      <div class="bg-white rounded-lg shadow p-6">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Produtos por Categoria</h3>
        <div class="h-64">
          <BarChart :data="categoriesChartData" />
        </div>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Status dos Produtos</h3>
        <div class="h-64">
          <PieChart :data="activeStatusChartData" />
        </div>
      </div>
    </div>
  </div>
</template>
