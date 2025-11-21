<script setup>
import { computed } from 'vue'
import PieChart from '../charts/PieChart.vue'

const props = defineProps({
  report: {
    type: Object,
    required: true
  }
})

const activeStatusChartData = computed(() => {
  return {
    labels: ['Ativos', 'Inativos'],
    datasets: [{
      data: [props.report.clientesAtivos, props.report.clientesInativos],
      backgroundColor: ['#10B981', '#EF4444']
    }]
  }
})

const activePercentage = computed(() => {
  const total = props.report.totalClientes
  if (total === 0) return 0
  return Math.round((props.report.clientesAtivos / total) * 100)
})
</script>

<template>
  <div class="space-y-6">
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <div class="bg-white rounded-lg shadow p-6">
        <div class="text-sm text-gray-600">Total de Clientes</div>
        <div class="text-2xl font-bold text-gray-900 mt-2">{{ report.totalClientes }}</div>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <div class="text-sm text-gray-600">Clientes Ativos</div>
        <div class="text-2xl font-bold text-green-600 mt-2">{{ report.clientesAtivos }}</div>
      </div>
      <div class="bg-white rounded-lg shadow p-6">
        <div class="text-sm text-gray-600">Taxa de Ativacao</div>
        <div class="text-2xl font-bold text-primary-600 mt-2">{{ activePercentage }}%</div>
      </div>
    </div>

    <div class="bg-white rounded-lg shadow p-6">
      <h3 class="text-lg font-semibold text-gray-900 mb-4">Status dos Clientes</h3>
      <div class="h-64 flex justify-center">
        <div class="w-full max-w-md">
          <PieChart :data="activeStatusChartData" />
        </div>
      </div>
    </div>
  </div>
</template>
