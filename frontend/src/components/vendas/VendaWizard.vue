<template>
  <div class="max-w-4xl mx-auto">
    <!-- Stepper -->
    <div class="mb-8">
      <div class="flex items-center justify-between">
        <div
          v-for="(step, index) in steps"
          :key="index"
          class="flex-1 flex items-center"
        >
          <div class="flex items-center">
            <div
              :class="[
                'w-10 h-10 rounded-full flex items-center justify-center font-semibold',
                currentStep >= index
                  ? 'bg-primary-600 text-white'
                  : 'bg-gray-200 text-gray-600'
              ]"
            >
              {{ index + 1 }}
            </div>
            <div class="ml-3">
              <div class="text-sm font-medium" :class="currentStep >= index ? 'text-primary-600' : 'text-gray-500'">
                {{ step.title }}
              </div>
            </div>
          </div>
          <div
            v-if="index < steps.length - 1"
            class="flex-1 h-0.5 mx-4"
            :class="currentStep > index ? 'bg-primary-600' : 'bg-gray-200'"
          ></div>
        </div>
      </div>
    </div>

    <!-- Conteúdo das Etapas -->
    <div class="bg-white rounded-lg shadow p-6">
      <!-- Etapa 1: Selecionar Cliente -->
      <div v-if="currentStep === 0" class="space-y-4">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Selecione o Cliente</h3>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Cliente *</label>
          <select
            v-model="form.clienteId"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
            :class="{ 'border-red-500': errors.clienteId }"
          >
            <option value="">Selecione um cliente</option>
            <option v-for="cliente in clientesAtivos" :key="cliente.id" :value="cliente.id">
              {{ cliente.nome }} - {{ cliente.cpf }}
            </option>
          </select>
          <p v-if="errors.clienteId" class="mt-1 text-sm text-red-600">{{ errors.clienteId }}</p>
        </div>

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Observações</label>
          <textarea
            v-model="form.observacoes"
            rows="3"
            placeholder="Observações sobre a venda (opcional)"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
          ></textarea>
        </div>
      </div>

      <!-- Etapa 2: Adicionar Itens -->
      <div v-if="currentStep === 1" class="space-y-4">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Adicione os Itens</h3>

        <!-- Formulário de Novo Item -->
        <div class="bg-gray-50 p-4 rounded-lg space-y-4">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Tipo *</label>
              <select
                v-model="novoItem.tipoItem"
                @change="limparSelecaoItem"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
              >
                <option value="">Selecione o tipo</option>
                <option value="PRODUTO">Produto</option>
                <option value="SERVICO">Serviço</option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Item *</label>
              <select
                v-model="novoItem.itemId"
                @change="selecionarItem"
                :disabled="!novoItem.tipoItem"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
              >
                <option value="">Selecione o item</option>
                <option
                  v-for="item in itensDisponiveis"
                  :key="item.id"
                  :value="item.id"
                >
                  {{ item.nome }} - R$ {{ formatCurrency(item.preco) }}
                </option>
              </select>
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Quantidade *</label>
              <input
                v-model.number="novoItem.quantidade"
                type="number"
                min="1"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
              />
            </div>

            <div>
              <label class="block text-sm font-medium text-gray-700 mb-2">Preço Unitário *</label>
              <input
                v-model.number="novoItem.precoUnitario"
                type="number"
                step="0.01"
                min="0"
                class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
              />
            </div>
          </div>

          <button
            @click="adicionarItem"
            :disabled="!podeAdicionarItem"
            class="w-full px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
          >
            Adicionar Item
          </button>
        </div>

        <!-- Lista de Itens -->
        <div v-if="form.itens.length > 0">
          <ItemVendaTable
            :itens="form.itens"
            :editable="true"
            @remove-item="removerItem"
          />
        </div>
        <div v-else class="text-center py-8 text-gray-500">
          Nenhum item adicionado ainda
        </div>

        <p v-if="errors.itens" class="mt-2 text-sm text-red-600">{{ errors.itens }}</p>
      </div>

      <!-- Etapa 3: Confirmação -->
      <div v-if="currentStep === 2" class="space-y-6">
        <h3 class="text-lg font-semibold text-gray-900 mb-4">Revise e Confirme a Venda</h3>

        <div class="bg-gray-50 p-4 rounded-lg space-y-3">
          <div class="flex justify-between">
            <span class="text-sm font-medium text-gray-700">Cliente:</span>
            <span class="text-sm text-gray-900">{{ clienteSelecionado?.nome }}</span>
          </div>
          <div class="flex justify-between">
            <span class="text-sm font-medium text-gray-700">Data:</span>
            <span class="text-sm text-gray-900">{{ new Date().toLocaleDateString('pt-BR') }}</span>
          </div>
          <div v-if="form.observacoes" class="flex justify-between">
            <span class="text-sm font-medium text-gray-700">Observações:</span>
            <span class="text-sm text-gray-900">{{ form.observacoes }}</span>
          </div>
        </div>

        <div>
          <h4 class="text-md font-semibold text-gray-900 mb-3">Itens da Venda</h4>
          <ItemVendaTable :itens="form.itens" :editable="false" />
        </div>
      </div>
    </div>

    <!-- Botões de Navegação -->
    <div class="mt-6 flex justify-between">
      <button
        v-if="currentStep > 0"
        @click="voltarEtapa"
        class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors"
      >
        Voltar
      </button>
      <div v-else></div>

      <div class="flex space-x-2">
        <button
          @click="$emit('cancel')"
          class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50 transition-colors"
        >
          Cancelar
        </button>
        <button
          v-if="currentStep < steps.length - 1"
          @click="proximaEtapa"
          class="px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 transition-colors"
        >
          Próxima Etapa
        </button>
        <button
          v-else
          @click="finalizarVenda"
          :disabled="loading"
          class="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 disabled:bg-gray-300 disabled:cursor-not-allowed transition-colors"
        >
          {{ loading ? 'Processando...' : 'Finalizar Venda' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import ItemVendaTable from './ItemVendaTable.vue'
import { useClientesStore } from '@/stores/clientes'
import { useProdutosStore } from '@/stores/produtos'
import { useServicosStore } from '@/stores/servicos'
import { formatCurrency } from '@/utils/formatters'

export default {
  name: 'VendaWizard',
  components: {
    ItemVendaTable
  },
  emits: ['submit', 'cancel'],
  setup(props, { emit }) {
    const clientesStore = useClientesStore()
    const produtosStore = useProdutosStore()
    const servicosStore = useServicosStore()

    const steps = [
      { title: 'Cliente' },
      { title: 'Itens' },
      { title: 'Confirmação' }
    ]

    const currentStep = ref(0)
    const loading = ref(false)
    const errors = ref({})

    const form = ref({
      clienteId: '',
      dataVenda: new Date().toISOString(),
      observacoes: '',
      itens: []
    })

    const novoItem = ref({
      tipoItem: '',
      itemId: '',
      itemNome: '',
      quantidade: 1,
      precoUnitario: 0
    })

    const clientesAtivos = computed(() =>
      clientesStore.clientes.filter(c => c.ativo)
    )

    const clienteSelecionado = computed(() =>
      clientesStore.clientes.find(c => c.id === form.value.clienteId)
    )

    const itensDisponiveis = computed(() => {
      if (novoItem.value.tipoItem === 'PRODUTO') {
        return produtosStore.produtos.filter(p => p.ativo)
      } else if (novoItem.value.tipoItem === 'SERVICO') {
        return servicosStore.servicos.filter(s => s.ativo)
      }
      return []
    })

    const podeAdicionarItem = computed(() =>
      novoItem.value.tipoItem &&
      novoItem.value.itemId &&
      novoItem.value.quantidade > 0 &&
      novoItem.value.precoUnitario > 0
    )

    onMounted(async () => {
      await Promise.all([
        clientesStore.fetchClientes(),
        produtosStore.fetchProdutos(),
        servicosStore.fetchServicos()
      ])
    })

    function limparSelecaoItem() {
      novoItem.value.itemId = ''
      novoItem.value.itemNome = ''
      novoItem.value.precoUnitario = 0
    }

    function selecionarItem() {
      const items = novoItem.value.tipoItem === 'PRODUTO'
        ? produtosStore.produtos
        : servicosStore.servicos

      const item = items.find(i => i.id === novoItem.value.itemId)
      if (item) {
        novoItem.value.itemNome = item.nome
        novoItem.value.precoUnitario = item.preco
      }
    }

    function adicionarItem() {
      if (!podeAdicionarItem.value) return

      form.value.itens.push({
        tipoItem: novoItem.value.tipoItem,
        itemId: novoItem.value.itemId,
        itemNome: novoItem.value.itemNome,
        quantidade: novoItem.value.quantidade,
        precoUnitario: novoItem.value.precoUnitario
      })

      novoItem.value = {
        tipoItem: '',
        itemId: '',
        itemNome: '',
        quantidade: 1,
        precoUnitario: 0
      }
    }

    function removerItem(index) {
      form.value.itens.splice(index, 1)
    }

    function validarEtapa() {
      errors.value = {}

      if (currentStep.value === 0) {
        if (!form.value.clienteId) {
          errors.value.clienteId = 'Selecione um cliente'
          return false
        }
      }

      if (currentStep.value === 1) {
        if (form.value.itens.length === 0) {
          errors.value.itens = 'Adicione pelo menos um item à venda'
          return false
        }
      }

      return true
    }

    function proximaEtapa() {
      if (validarEtapa()) {
        currentStep.value++
      }
    }

    function voltarEtapa() {
      currentStep.value--
    }

    async function finalizarVenda() {
      if (!validarEtapa()) return

      loading.value = true
      try {
        await emit('submit', form.value)
      } finally {
        loading.value = false
      }
    }

    return {
      steps,
      currentStep,
      loading,
      errors,
      form,
      novoItem,
      clientesAtivos,
      clienteSelecionado,
      itensDisponiveis,
      podeAdicionarItem,
      limparSelecaoItem,
      selecionarItem,
      adicionarItem,
      removerItem,
      proximaEtapa,
      voltarEtapa,
      finalizarVenda,
      formatCurrency
    }
  }
}
</script>
