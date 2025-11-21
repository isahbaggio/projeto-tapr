<template>
  <div class="overflow-x-auto">
    <table class="min-w-full divide-y divide-gray-200">
      <thead class="bg-gray-50">
        <tr>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
            Tipo
          </th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
            Item
          </th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
            Quantidade
          </th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
            Preço Unit.
          </th>
          <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
            Subtotal
          </th>
          <th v-if="editable" class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
            Ações
          </th>
        </tr>
      </thead>
      <tbody class="bg-white divide-y divide-gray-200">
        <tr v-for="(item, index) in itens" :key="index">
          <td class="px-6 py-4 whitespace-nowrap">
            <span
              :class="[
                'px-2 inline-flex text-xs leading-5 font-semibold rounded-full',
                item.tipoItem === 'PRODUTO'
                  ? 'bg-blue-100 text-blue-800'
                  : 'bg-green-100 text-green-800'
              ]"
            >
              {{ item.tipoItem === 'PRODUTO' ? 'Produto' : 'Serviço' }}
            </span>
          </td>
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
            {{ item.itemNome }}
          </td>
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
            {{ item.quantidade }}
          </td>
          <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-600">
            R$ {{ formatCurrency(item.precoUnitario) }}
          </td>
          <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
            R$ {{ formatCurrency(item.subtotal || (item.quantidade * item.precoUnitario)) }}
          </td>
          <td v-if="editable" class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
            <button
              @click="$emit('remove-item', index)"
              class="text-red-600 hover:text-red-900"
            >
              Remover
            </button>
          </td>
        </tr>
        <tr v-if="itens.length === 0">
          <td :colspan="editable ? 6 : 5" class="px-6 py-4 text-center text-gray-500">
            Nenhum item adicionado
          </td>
        </tr>
      </tbody>
      <tfoot v-if="itens.length > 0" class="bg-gray-50">
        <tr>
          <td colspan="4" class="px-6 py-4 text-right font-bold text-gray-900">
            Total:
          </td>
          <td class="px-6 py-4 font-bold text-lg text-primary-600">
            R$ {{ formatCurrency(total) }}
          </td>
          <td v-if="editable"></td>
        </tr>
      </tfoot>
    </table>
  </div>
</template>

<script>
import { computed } from 'vue'
import { formatCurrency } from '@/utils/formatters'

export default {
  name: 'ItemVendaTable',
  props: {
    itens: {
      type: Array,
      required: true
    },
    editable: {
      type: Boolean,
      default: false
    }
  },
  emits: ['remove-item'],
  setup(props) {
    const total = computed(() => {
      return props.itens.reduce((sum, item) => {
        const subtotal = item.subtotal || (item.quantidade * item.precoUnitario)
        return sum + subtotal
      }, 0)
    })

    return {
      total,
      formatCurrency
    }
  }
}
</script>
