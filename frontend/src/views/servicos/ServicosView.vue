<script setup>
import { ref, onMounted, computed } from 'vue'
import { useServicosStore } from '@/stores/servicos'
import { useNotificationStore } from '@/stores/notification'
import { formatCurrency, formatDuration } from '@/utils/formatters'
import BaseButton from '@/components/ui/BaseButton.vue'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseModal from '@/components/ui/BaseModal.vue'
import BaseTextarea from '@/components/ui/BaseTextarea.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import EmptyState from '@/components/common/EmptyState.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'

const servicosStore = useServicosStore()
const notification = useNotificationStore()

const searchQuery = ref('')
const showModal = ref(false)
const showDeleteDialog = ref(false)
const editingServico = ref(null)
const deletingServicoId = ref(null)

const form = ref({
  nome: '',
  descricao: '',
  preco: '',
  duracao: '',
  ativo: true
})

const errors = ref({})

const filteredServicos = computed(() => {
  if (!searchQuery.value) return servicosStore.servicos

  const query = searchQuery.value.toLowerCase()
  return servicosStore.servicos.filter(servico =>
    servico.nome?.toLowerCase().includes(query)
  )
})

onMounted(() => {
  servicosStore.fetchServicos()
})

function openCreateModal() {
  editingServico.value = null
  form.value = {
    nome: '',
    descricao: '',
    preco: '',
    duracao: '',
    ativo: true
  }
  errors.value = {}
  showModal.value = true
}

function openEditModal(servico) {
  editingServico.value = servico
  form.value = {
    nome: servico.nome,
    descricao: servico.descricao,
    preco: servico.preco,
    duracao: servico.duracao,
    ativo: servico.ativo
  }
  errors.value = {}
  showModal.value = true
}

function openDeleteDialog(servicoId) {
  deletingServicoId.value = servicoId
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

  if (!form.value.duracao || isNaN(form.value.duracao) || parseInt(form.value.duracao) <= 0) {
    errors.value.duracao = 'Duração deve ser um valor válido maior que zero'
  }

  return Object.keys(errors.value).length === 0
}

async function handleSubmit() {
  if (!validateForm()) return

  try {
    const data = {
      ...form.value,
      preco: parseFloat(form.value.preco),
      duracao: parseInt(form.value.duracao)
    }

    if (editingServico.value) {
      await servicosStore.updateServico(editingServico.value.id, data)
      notification.success('Serviço atualizado com sucesso')
    } else {
      await servicosStore.createServico(data)
      notification.success('Serviço criado com sucesso')
    }

    showModal.value = false
  } catch (error) {
    notification.error(error.response?.data?.message || 'Erro ao salvar serviço')
  }
}

async function handleDelete() {
  try {
    await servicosStore.deleteServico(deletingServicoId.value)
    notification.success('Serviço deletado com sucesso')
    showDeleteDialog.value = false
  } catch (error) {
    notification.error(error.response?.data?.message || 'Erro ao deletar serviço')
  }
}
</script>

<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-900">Serviços</h1>
      <BaseButton @click="openCreateModal" variant="primary">
        Novo Serviço
      </BaseButton>
    </div>

    <div class="bg-white rounded-lg shadow-md">
      <div class="p-4 border-b border-gray-200">
        <BaseInput
          v-model="searchQuery"
          type="text"
          placeholder="Buscar por nome..."
        />
      </div>

      <LoadingSpinner v-if="servicosStore.loading" class="py-12" />

      <EmptyState
        v-else-if="filteredServicos.length === 0 && !searchQuery"
        title="Nenhum serviço cadastrado"
        message="Comece criando seu primeiro serviço"
        icon="cog"
      >
        <BaseButton @click="openCreateModal" variant="primary">
          Criar Serviço
        </BaseButton>
      </EmptyState>

      <EmptyState
        v-else-if="filteredServicos.length === 0"
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
                Preço
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Duração
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
            <tr v-for="servico in filteredServicos" :key="servico.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm font-medium text-gray-900">{{ servico.nome }}</div>
                <div class="text-sm text-gray-500">{{ servico.descricao }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="text-sm font-semibold text-gray-900">{{ formatCurrency(servico.preco) }}</span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="text-sm text-gray-900">{{ formatDuration(servico.duracao) }}</span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span
                  :class="[
                    'px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full',
                    servico.ativo ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                  ]"
                >
                  {{ servico.ativo ? 'Ativo' : 'Inativo' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button
                  @click="openEditModal(servico)"
                  class="text-primary-600 hover:text-primary-900 mr-3"
                >
                  Editar
                </button>
                <button
                  @click="openDeleteDialog(servico.id)"
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

    <BaseModal :show="showModal" @close="showModal = false" :title="editingServico ? 'Editar Serviço' : 'Novo Serviço'">
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <BaseInput
          v-model="form.nome"
          label="Nome"
          placeholder="Nome do serviço"
          :error="errors.nome"
          required
        />

        <BaseTextarea
          v-model="form.descricao"
          label="Descrição"
          placeholder="Descrição do serviço"
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
          v-model="form.duracao"
          type="number"
          label="Duração (minutos)"
          placeholder="60"
          :error="errors.duracao"
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
            Serviço ativo
          </label>
        </div>
      </form>

      <template #footer>
        <BaseButton @click="showModal = false" variant="secondary">
          Cancelar
        </BaseButton>
        <BaseButton @click="handleSubmit" variant="primary" :loading="servicosStore.loading">
          {{ editingServico ? 'Atualizar' : 'Criar' }}
        </BaseButton>
      </template>
    </BaseModal>

    <ConfirmDialog
      :show="showDeleteDialog"
      title="Confirmar exclusão"
      message="Tem certeza que deseja deletar este serviço? Esta ação não pode ser desfeita."
      confirmText="Deletar"
      @confirm="handleDelete"
      @cancel="showDeleteDialog = false"
    />
  </div>
</template>
