<script setup>
import { ref, onMounted, computed } from 'vue'
import { useClientesStore } from '@/stores/clientes'
import { useNotificationStore } from '@/stores/notification'
import { formatPhone, formatCPF } from '@/utils/formatters'
import { isValidCPF, isValidEmail, isValidPhone } from '@/utils/validators'
import BaseButton from '@/components/ui/BaseButton.vue'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseModal from '@/components/ui/BaseModal.vue'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import EmptyState from '@/components/common/EmptyState.vue'
import ConfirmDialog from '@/components/common/ConfirmDialog.vue'

const clientesStore = useClientesStore()
const notification = useNotificationStore()

const searchQuery = ref('')
const showModal = ref(false)
const showDeleteDialog = ref(false)
const editingCliente = ref(null)
const deletingClienteId = ref(null)

const form = ref({
  nome: '',
  telefone: '',
  email: '',
  cpf: '',
  endereco: '',
  ativo: true
})

const errors = ref({})

const filteredClientes = computed(() => {
  if (!searchQuery.value) return clientesStore.clientes

  const query = searchQuery.value.toLowerCase()
  return clientesStore.clientes.filter(cliente =>
    cliente.nome?.toLowerCase().includes(query) ||
    cliente.email?.toLowerCase().includes(query) ||
    cliente.cpf?.includes(query)
  )
})

onMounted(() => {
  clientesStore.fetchClientes()
})

function openCreateModal() {
  editingCliente.value = null
  form.value = {
    nome: '',
    telefone: '',
    email: '',
    cpf: '',
    endereco: '',
    ativo: true
  }
  errors.value = {}
  showModal.value = true
}

function openEditModal(cliente) {
  editingCliente.value = cliente
  form.value = {
    nome: cliente.nome,
    telefone: cliente.telefone,
    email: cliente.email,
    cpf: cliente.cpf,
    endereco: cliente.endereco,
    ativo: cliente.ativo
  }
  errors.value = {}
  showModal.value = true
}

function openDeleteDialog(clienteId) {
  deletingClienteId.value = clienteId
  showDeleteDialog.value = true
}

function validateForm() {
  errors.value = {}

  if (!form.value.nome) {
    errors.value.nome = 'Nome é obrigatório'
  }

  if (!form.value.telefone) {
    errors.value.telefone = 'Telefone é obrigatório'
  } else if (!isValidPhone(form.value.telefone)) {
    errors.value.telefone = 'Telefone inválido'
  }

  if (!form.value.email) {
    errors.value.email = 'Email é obrigatório'
  } else if (!isValidEmail(form.value.email)) {
    errors.value.email = 'Email inválido'
  }

  if (!form.value.cpf) {
    errors.value.cpf = 'CPF é obrigatório'
  } else if (!isValidCPF(form.value.cpf)) {
    errors.value.cpf = 'CPF inválido'
  }

  return Object.keys(errors.value).length === 0
}

async function handleSubmit() {
  if (!validateForm()) return

  try {
    const data = {
      ...form.value,
      telefone: form.value.telefone.replace(/\D/g, ''),
      cpf: form.value.cpf.replace(/\D/g, '')
    }

    if (editingCliente.value) {
      await clientesStore.updateCliente(editingCliente.value.id, data)
      notification.success('Cliente atualizado com sucesso')
    } else {
      await clientesStore.createCliente(data)
      notification.success('Cliente criado com sucesso')
    }

    showModal.value = false
  } catch (error) {
    notification.error(error.response?.data?.message || 'Erro ao salvar cliente')
  }
}

async function handleDelete() {
  try {
    await clientesStore.deleteCliente(deletingClienteId.value)
    notification.success('Cliente deletado com sucesso')
    showDeleteDialog.value = false
  } catch (error) {
    notification.error(error.response?.data?.message || 'Erro ao deletar cliente')
  }
}

function maskPhone(value) {
  const cleaned = value.replace(/\D/g, '')
  const match = cleaned.match(/^(\d{0,2})(\d{0,5})(\d{0,4})$/)
  if (match) {
    let formatted = ''
    if (match[1]) formatted += `(${match[1]}`
    if (match[2]) formatted += `) ${match[2]}`
    if (match[3]) formatted += `-${match[3]}`
    return formatted
  }
  return value
}

function maskCPF(value) {
  const cleaned = value.replace(/\D/g, '')
  const match = cleaned.match(/^(\d{0,3})(\d{0,3})(\d{0,3})(\d{0,2})$/)
  if (match) {
    let formatted = match[1]
    if (match[2]) formatted += `.${match[2]}`
    if (match[3]) formatted += `.${match[3]}`
    if (match[4]) formatted += `-${match[4]}`
    return formatted
  }
  return value
}

function handlePhoneInput(event) {
  form.value.telefone = maskPhone(event.target.value)
}

function handleCPFInput(event) {
  form.value.cpf = maskCPF(event.target.value)
}
</script>

<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold text-gray-900">Clientes</h1>
      <BaseButton @click="openCreateModal" variant="primary">
        Novo Cliente
      </BaseButton>
    </div>

    <div class="bg-white rounded-lg shadow-md">
      <div class="p-4 border-b border-gray-200">
        <BaseInput
          v-model="searchQuery"
          type="text"
          placeholder="Buscar por nome, email ou CPF..."
        />
      </div>

      <LoadingSpinner v-if="clientesStore.loading" class="py-12" />

      <EmptyState
        v-else-if="filteredClientes.length === 0 && !searchQuery"
        title="Nenhum cliente cadastrado"
        message="Comece criando seu primeiro cliente"
        icon="users"
      >
        <BaseButton @click="openCreateModal" variant="primary">
          Criar Cliente
        </BaseButton>
      </EmptyState>

      <EmptyState
        v-else-if="filteredClientes.length === 0"
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
                Contato
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                CPF
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
            <tr v-for="cliente in filteredClientes" :key="cliente.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm font-medium text-gray-900">{{ cliente.nome }}</div>
                <div class="text-sm text-gray-500">{{ cliente.endereco }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm text-gray-900">{{ formatPhone(cliente.telefone) }}</div>
                <div class="text-sm text-gray-500">{{ cliente.email }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span class="text-sm text-gray-900">{{ formatCPF(cliente.cpf) }}</span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span
                  :class="[
                    'px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full',
                    cliente.ativo ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                  ]"
                >
                  {{ cliente.ativo ? 'Ativo' : 'Inativo' }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button
                  @click="openEditModal(cliente)"
                  class="text-primary-600 hover:text-primary-900 mr-3"
                >
                  Editar
                </button>
                <button
                  @click="openDeleteDialog(cliente.id)"
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

    <BaseModal :show="showModal" @close="showModal = false" :title="editingCliente ? 'Editar Cliente' : 'Novo Cliente'">
      <form @submit.prevent="handleSubmit" class="space-y-4">
        <BaseInput
          v-model="form.nome"
          label="Nome"
          placeholder="Nome completo"
          :error="errors.nome"
          required
        />

        <BaseInput
          v-model="form.telefone"
          type="tel"
          label="Telefone"
          placeholder="(11) 98888-7777"
          :error="errors.telefone"
          @input="handlePhoneInput"
          required
        />

        <BaseInput
          v-model="form.email"
          type="email"
          label="Email"
          placeholder="cliente@email.com"
          :error="errors.email"
          required
        />

        <BaseInput
          v-model="form.cpf"
          label="CPF"
          placeholder="111.222.333-44"
          :error="errors.cpf"
          @input="handleCPFInput"
          required
        />

        <BaseInput
          v-model="form.endereco"
          label="Endereço"
          placeholder="Rua, número, complemento"
        />

        <div class="flex items-center">
          <input
            id="ativo"
            v-model="form.ativo"
            type="checkbox"
            class="w-4 h-4 text-primary-600 border-gray-300 rounded focus:ring-primary-500"
          />
          <label for="ativo" class="ml-2 text-sm font-medium text-gray-700">
            Cliente ativo
          </label>
        </div>
      </form>

      <template #footer>
        <BaseButton @click="showModal = false" variant="secondary">
          Cancelar
        </BaseButton>
        <BaseButton @click="handleSubmit" variant="primary" :loading="clientesStore.loading">
          {{ editingCliente ? 'Atualizar' : 'Criar' }}
        </BaseButton>
      </template>
    </BaseModal>

    <ConfirmDialog
      :show="showDeleteDialog"
      title="Confirmar exclusão"
      message="Tem certeza que deseja deletar este cliente? Esta ação não pode ser desfeita."
      confirmText="Deletar"
      @confirm="handleDelete"
      @cancel="showDeleteDialog = false"
    />
  </div>
</template>
