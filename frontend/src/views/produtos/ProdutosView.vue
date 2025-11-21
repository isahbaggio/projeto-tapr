<script setup>
import { ref, onMounted, computed } from 'vue'
import { useProdutosStore } from '@/stores/produtos'
import { useNotificationStore } from '@/stores/notification'
import { formatCurrency } from '@/utils/formatters'
import BaseButton from '@/components/ui/BaseButton.vue'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseModal from '@/components/ui/BaseModal.vue'
import BaseTextarea from '@/components/ui/BaseTextarea.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import EmptyState from '@/components/common/EmptyState.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'

const produtosStore = useProdutosStore()
const notification = useNotificationStore()

const searchQuery = ref('')
const showModal = ref(false)
const showDeleteDialog = ref(false)
const editingProduto = ref(null)
const deletingProdutoId = ref(null)

const form = ref({
  nome: '',
  descricao: '',
  preco: '',
  categoria: '',
  ativo: true
})

const errors = ref({})

const filteredProdutos = computed(() => {
  if (!searchQuery.value) return produtosStore.produtos

  const query = searchQuery.value.toLowerCase()
  return produtosStore.produtos.filter(produto =>
    produto.nome?.toLowerCase().includes(query) ||
    produto.categoria?.toLowerCase().includes(query)
  )
})

onMounted(() => {
  produtosStore.fetchProdutos()
})

function openCreateModal() {
  editingProduto.value = null
  form.value = {
    nome: '',
    descricao: '',
    preco: '',
    categoria: '',
    ativo: true
  }
  errors.value = {}
  showModal.value = true
}

function openEditModal(produto) {
  editingProduto.value = produto
  form.value = {
    nome: produto.nome,
    descricao: produto.descricao,
    preco: produto.preco,
    categoria: produto.categoria,
    ativo: produto.ativo
  }
  errors.value = {}
  showModal.value = true
}

function openDeleteDialog(produtoId) {
  deletingProdutoId.value = produtoId
  showDeleteDialog.value = true
}

function validateForm() {
  errors.value = {}

  if (!form.value.nome) {
    errors.value.nome = 'Nome é obrigatório'
  }

  if (!form.value.preco || isNaN(form.value.preco) || parseFloat(form.value.preco) <= 0) {
    errors.value.preco = 'Preço deve ser um valor válido maior que zero'
  }

  if (!form.value.categoria) {
    errors.value.categoria = 'Categoria é obrigatória'
  }

  return Object.keys(errors.value).length === 0
}

async function handleSubmit() {
  if (!validateForm()) return

  try {
    const data = {
      ...form.value,
      preco: parseFloat(form.value.preco)
    }

    if (editingProduto.value) {
      await produtosStore.updateProduto(editingProduto.value.id, data)
      notification.success('Produto atualizado com sucesso')
    } else {
      await produtosStore.createProduto(data)
      notification.success('Produto criado com sucesso')
    }

    showModal.value = false
  } catch (error) {
    notification.error(error.response?.data?.message || 'Erro ao salvar produto')
  }
}

async function handleDelete() {
  try {
    await produtosStore.deleteProduto(deletingProdutoId.value)
    notification.success('Produto deletado com sucesso')
    showDeleteDialog.value = false
  } catch (error) {
    notification.error(error.response?.data?.message || 'Erro ao deletar produto')
  }
}
</script>

<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-900">Produtos</h1>
      <BaseButton @click="openCreateModal" variant="primary">
        Novo Produto
      </BaseButton>
    </div>

    <div class="bg-white rounded-lg shadow-md">
      <div class="p-4 border-b border-gray-200">
        <BaseInput
          v-model="searchQuery"
          type="text"
          placeholder="Buscar por nome ou categoria..."
        />
      </div>

      <LoadingSpinner v-if="produtosStore.loading" class="py-12" />

      <EmptyState
        v-else-if="filteredProdutos.length === 0 && !searchQuery"
        title="Nenhum produto cadastrado"
        message="Comece criando seu primeiro produto"
        icon="box"
      >
        <BaseButton @click="openCreateModal" variant="primary">
          Criar Produto
        </BaseButton>
      </EmptyState>

      <EmptyState
        v-else-if="filteredProdutos.length === 0"
        title="Nenhum resultado encontrado"
        message="Tente ajustar sua busca"
        icon="search"
      />

      <div v-else class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Nome
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Categoria
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Preço
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
            <tr v-for="produto in filteredProdutos" :key="produto.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm font-medium text-gray-900">{{ produto.nome }}</div>
                <div class="text-sm text-gray-500">{{ produto.descricao }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="text-sm text-gray-900">{{ produto.categoria }}</span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="text-sm font-semibold text-gray-900">{{ formatCurrency(produto.preco) }}</span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span
                  :class="[
                    'px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full',
                    produto.ativo ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                  ]"
                >
                  {{ produto.ativo ? 'Ativo' : 'Inativo' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button
                  @click="openEditModal(produto)"
                  class="text-primary-600 hover:text-primary-900 mr-3"
                >
                  Editar
                </button>
                <button
                  @click="openDeleteDialog(produto.id)"
                  class="text-red-600 hover:text-red-900"
                >
                  Deletar
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <BaseModal :show="showModal" @close="showModal = false" :title="editingProduto ? 'Editar Produto' : 'Novo Produto'">
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <BaseInput
          v-model="form.nome"
          label="Nome"
          placeholder="Nome do produto"
          :error="errors.nome"
          required
        />

        <BaseTextarea
          v-model="form.descricao"
          label="Descrição"
          placeholder="Descrição do produto"
          :rows="3"
        />

        <BaseInput
          v-model="form.preco"
          type="number"
          step="0.01"
          label="Preço"
          placeholder="0.00"
          :error="errors.preco"
          required
        />

        <BaseInput
          v-model="form.categoria"
          label="Categoria"
          placeholder="Categoria do produto"
          :error="errors.categoria"
          required
        />

        <div class="flex items-center">
          <input
            id="ativo"
            v-model="form.ativo"
            type="checkbox"
            class="w-4 h-4 text-primary-600 border-gray-300 rounded focus:ring-primary-500"
          />
          <label for="ativo" class="ml-2 text-sm font-medium text-gray-700">
            Produto ativo
          </label>
        </div>
      </form>

      <template #footer>
        <BaseButton @click="showModal = false" variant="secondary">
          Cancelar
        </BaseButton>
        <BaseButton @click="handleSubmit" variant="primary" :loading="produtosStore.loading">
          {{ editingProduto ? 'Atualizar' : 'Criar' }}
        </BaseButton>
      </template>
    </BaseModal>

    <ConfirmDialog
      :show="showDeleteDialog"
      title="Confirmar exclusão"
      message="Tem certeza que deseja deletar este produto? Esta ação não pode ser desfeita."
      confirmText="Deletar"
      @confirm="handleDelete"
      @cancel="showDeleteDialog = false"
    />
  </div>
</template>
