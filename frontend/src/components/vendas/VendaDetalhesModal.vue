<template>
  <BaseModal :show="show" @close="$emit('close')" size="xl">
    <template #header>
      <h3 class="text-lg font-semibold text-gray-900">
        Detalhes da Venda #{{ venda?.id?.substring(0, 8).toUpperCase() }}
      </h3>
    </template>

    <div v-if="venda" class="space-y-6">
      <!-- Status -->
      <div v-if="venda.cancelada" class="bg-red-50 border border-red-200 rounded-lg p-4">
        <div class="flex items-center">
          <span class="text-red-600 font-semibold">⚠️ Esta venda foi cancelada</span>
        </div>
      </div>

      <!-- Informações da Venda -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label class="block text-sm font-medium text-gray-700">Data da Venda</label>
          <p class="mt-1 text-sm text-gray-900">{{ formatDate(venda.dataVenda) }}</p>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700">Cliente</label>
          <p class="mt-1 text-sm text-gray-900">{{ cliente?.nome || 'Carregando...' }}</p>
        </div>

        <div v-if="venda.observacoes" class="md:col-span-2">
          <label class="block text-sm font-medium text-gray-700">Observações</label>
          <p class="mt-1 text-sm text-gray-900">{{ venda.observacoes }}</p>
        </div>
      </div>

      <!-- Itens da Venda -->
      <div>
        <h4 class="text-md font-semibold text-gray-900 mb-3">Itens da Venda</h4>
        <ItemVendaTable :itens="venda.itens" :editable="false" />
      </div>
    </div>

    <template #footer>
      <div class="flex justify-between items-center w-full">
        <div class="flex space-x-2">
          <button
            v-if="!venda?.cancelada"
            @click="handleCancelar"
            class="px-4 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 transition-colors"
          >
            Cancelar Venda
          </button>
          <button
            @click="handleImprimir"
            class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
          >
            Imprimir Recibo
          </button>
        </div>
        <button
          @click="$emit('close')"
          class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors"
        >
          Fechar
        </button>
      </div>
    </template>
  </BaseModal>
</template>

<script>
import { ref, watch } from 'vue'
import BaseModal from '../ui/BaseModal.vue'
import ItemVendaTable from './ItemVendaTable.vue'
import { formatDate } from '@/utils/formatters'
import { gerarReciboPDF } from '@/utils/pdfGenerator'
import { clienteService } from '@/services/cliente.service'

export default {
  name: 'VendaDetalhesModal',
  components: {
    BaseModal,
    ItemVendaTable
  },
  props: {
    show: {
      type: Boolean,
      default: false
    },
    venda: {
      type: Object,
      default: null
    }
  },
  emits: ['close', 'cancelar'],
  setup(props, { emit }) {
    const cliente = ref(null)

    watch(() => props.venda, async (newVenda) => {
      if (newVenda && newVenda.clienteId) {
        try {
          cliente.value = await clienteService.getById(newVenda.clienteId)
        } catch (error) {
          console.error('Erro ao carregar cliente:', error)
        }
      }
    }, { immediate: true })

    function handleCancelar() {
      emit('cancelar', props.venda.id)
    }

    function handleImprimir() {
      gerarReciboPDF(props.venda, cliente.value)
    }

    return {
      cliente,
      handleCancelar,
      handleImprimir,
      formatDate
    }
  }
}
</script>
