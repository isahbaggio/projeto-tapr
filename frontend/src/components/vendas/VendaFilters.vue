<template>
  <div class="bg-white p-4 rounded-lg shadow space-y-4">
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
      <!-- Filtro por Cliente -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Cliente</label>
        <select
          v-model="localFilters.clienteId"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
        >
          <option value="">Todos os clientes</option>
          <option v-for="cliente in clientes" :key="cliente.id" :value="cliente.id">
            {{ cliente.nome }}
          </option>
        </select>
      </div>

      <!-- Filtro por Data Inicial -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Data Inicial</label>
        <input
          v-model="localFilters.dataInicio"
          type="date"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
        />
      </div>

      <!-- Filtro por Data Final -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Data Final</label>
        <input
          v-model="localFilters.dataFim"
          type="date"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
        />
      </div>

      <!-- Filtro por Status -->
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Status</label>
        <select
          v-model="localFilters.status"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
        >
          <option value="todas">Todas</option>
          <option value="ativas">Ativas</option>
          <option value="canceladas">Canceladas</option>
        </select>
      </div>
    </div>

    <!-- Filtro por Valor -->
    <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Valor Mínimo</label>
        <input
          v-model.number="localFilters.valorMinimo"
          type="number"
          step="0.01"
          min="0"
          placeholder="0.00"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
        />
      </div>

      <div>
        <label class="block text-sm font-medium text-gray-700 mb-1">Valor Máximo</label>
        <input
          v-model.number="localFilters.valorMaximo"
          type="number"
          step="0.01"
          min="0"
          placeholder="0.00"
          class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
        />
      </div>
    </div>

    <!-- Botões -->
    <div class="flex justify-end space-x-2">
      <button
        @click="limparFiltros"
        class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors"
      >
        Limpar Filtros
      </button>
      <button
        @click="aplicarFiltros"
        class="px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 transition-colors"
      >
        Aplicar Filtros
      </button>
    </div>
  </div>
</template>

<script>
import { ref, watch } from 'vue'

export default {
  name: 'VendaFilters',
  props: {
    clientes: {
      type: Array,
      default: () => []
    },
    filters: {
      type: Object,
      default: () => ({
        clienteId: '',
        dataInicio: '',
        dataFim: '',
        status: 'todas',
        valorMinimo: null,
        valorMaximo: null
      })
    }
  },
  emits: ['apply-filters', 'clear-filters'],
  setup(props, { emit }) {
    const localFilters = ref({ ...props.filters })

    watch(() => props.filters, (newFilters) => {
      localFilters.value = { ...newFilters }
    }, { deep: true })

    function aplicarFiltros() {
      emit('apply-filters', { ...localFilters.value })
    }

    function limparFiltros() {
      localFilters.value = {
        clienteId: '',
        dataInicio: '',
        dataFim: '',
        status: 'todas',
        valorMinimo: null,
        valorMaximo: null
      }
      emit('clear-filters')
    }

    return {
      localFilters,
      aplicarFiltros,
      limparFiltros
    }
  }
}
</script>
