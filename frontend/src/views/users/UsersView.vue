<script setup>
import { ref, onMounted } from 'vue'
import { userService } from '@/services/user.service'
import { useNotificationStore } from '@/stores/notification'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import EmptyState from '@/components/common/EmptyState.vue'
import BaseModal from '@/components/ui/BaseModal.vue'
import BaseInput from '@/components/ui/BaseInput.vue'

const notification = useNotificationStore()

const users = ref([])
const loading = ref(false)
const showEditModal = ref(false)
const showCreateModal = ref(false)
const editingUser = ref(null)
const editForm = ref({
  name: '',
  email: '',
  role: ''
})
const createForm = ref({
  name: '',
  email: '',
  password: '',
  role: 'ATENDENTE'
})
const errors = ref({})
const createErrors = ref({})
const pagination = ref({
  page: 0,
  size: 10,
  totalPages: 0,
  totalElements: 0
})

const roles = [
  { value: 'ATENDENTE', label: 'Atendente' },
  { value: 'MECANICO', label: 'Mecânico' },
  { value: 'GESTOR', label: 'Gestor' }
]

function getRoleLabel(roleValue) {
  const role = roles.find(r => r.value === roleValue)
  return role ? role.label : roleValue
}

function getRoleColor(roleValue) {
  switch(roleValue) {
    case 'GESTOR': return 'bg-purple-100 text-purple-800'
    case 'MECANICO': return 'bg-blue-100 text-blue-800'
    case 'ATENDENTE': return 'bg-green-100 text-green-800'
    default: return 'bg-gray-100 text-gray-800'
  }
}

async function fetchUsers() {
  try {
    loading.value = true
    const response = await userService.getAll(pagination.value.page, pagination.value.size)

    users.value = response.content
    pagination.value.totalPages = response.totalPages
    pagination.value.totalElements = response.totalElements
  } catch (error) {
    notification.error('Erro ao carregar usuários')
  } finally {
    loading.value = false
  }
}

function nextPage() {
  if (pagination.value.page < pagination.value.totalPages - 1) {
    pagination.value.page++
    fetchUsers()
  }
}

function prevPage() {
  if (pagination.value.page > 0) {
    pagination.value.page--
    fetchUsers()
  }
}

function openEditModal(user) {
  editingUser.value = user
  editForm.value = {
    name: user.name,
    email: user.email,
    role: user.role
  }
  errors.value = {}
  showEditModal.value = true
}

function closeEditModal() {
  showEditModal.value = false
  editingUser.value = null
  editForm.value = { name: '', email: '', role: '' }
  errors.value = {}
}

function validateEditForm() {
  errors.value = {}

  if (!editForm.value.name) {
    errors.value.name = 'Nome é obrigatório'
  }

  if (!editForm.value.email) {
    errors.value.email = 'Email é obrigatório'
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(editForm.value.email)) {
    errors.value.email = 'Email inválido'
  }

  return Object.keys(errors.value).length === 0
}

async function saveUser() {
  if (!validateEditForm()) return

  try {
    loading.value = true

    // Update user info
    await userService.update(editingUser.value.id, {
      name: editForm.value.name,
      email: editForm.value.email
    })

    // Update role if changed
    if (editForm.value.role !== editingUser.value.role) {
      await userService.changeRole(editingUser.value.id, editForm.value.role)
    }

    notification.success('Usuário atualizado com sucesso!')
    closeEditModal()
    await fetchUsers()
  } catch (error) {
    notification.error(error.response?.data?.message || 'Erro ao atualizar usuário')
  } finally {
    loading.value = false
  }
}

function openCreateModal() {
  createForm.value = {
    name: '',
    email: '',
    password: '',
    role: 'ATENDENTE'
  }
  createErrors.value = {}
  showCreateModal.value = true
}

function closeCreateModal() {
  showCreateModal.value = false
  createForm.value = {
    name: '',
    email: '',
    password: '',
    role: 'ATENDENTE'
  }
  createErrors.value = {}
}

function validateCreateForm() {
  createErrors.value = {}

  if (!createForm.value.name) {
    createErrors.value.name = 'Nome é obrigatório'
  }

  if (!createForm.value.email) {
    createErrors.value.email = 'Email é obrigatório'
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(createForm.value.email)) {
    createErrors.value.email = 'Email inválido'
  }

  if (!createForm.value.password) {
    createErrors.value.password = 'Senha é obrigatória'
  } else if (createForm.value.password.length < 8) {
    createErrors.value.password = 'Senha deve ter no mínimo 8 caracteres'
  }

  return Object.keys(createErrors.value).length === 0
}

async function createUser() {
  if (!validateCreateForm()) return

  try {
    loading.value = true

    await userService.create({
      name: createForm.value.name,
      email: createForm.value.email,
      password: createForm.value.password,
      role: createForm.value.role
    })

    notification.success('Usuário criado com sucesso!')
    closeCreateModal()
    await fetchUsers()
  } catch (error) {
    notification.error(error.response?.data?.message || 'Erro ao criar usuário')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchUsers()
})
</script>

<template>
  <div>
    <div class="mb-6 flex justify-between items-center">
      <div>
        <h1 class="text-2xl font-bold text-gray-900">Usuários</h1>
        <p class="text-gray-600">Gerenciar usuários do sistema</p>
      </div>
      <button
        @click="openCreateModal"
        class="px-4 py-2 bg-primary-600 text-white rounded-lg hover:bg-primary-700"
      >
        + Novo Usuário
      </button>
    </div>

    <div class="bg-white rounded-lg shadow-md">
      <LoadingSpinner v-if="loading" class="py-12" />

      <EmptyState
        v-else-if="users.length === 0"
        title="Nenhum usuário encontrado"
        message="Não há usuários cadastrados no sistema"
        icon="user"
      />

      <div v-else class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Nome
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Email
              </th>
              <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                Cargo
              </th>
              <th class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">
                Ações
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="user in users" :key="user.id" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm font-medium text-gray-900">{{ user.name }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="text-sm text-gray-900">{{ user.email }}</div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span
                  :class="[
                    'px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full',
                    getRoleColor(user.role)
                  ]"
                >
                  {{ getRoleLabel(user.role) }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                <button
                  @click="openEditModal(user)"
                  class="text-primary-600 hover:text-primary-900"
                >
                  Editar
                </button>
              </td>
            </tr>
          </tbody>
        </table>

        <div v-if="pagination.totalPages > 1" class="bg-gray-50 px-6 py-3 flex items-center justify-between border-t border-gray-200">
          <div class="text-sm text-gray-700">
            Página {{ pagination.page + 1 }} de {{ pagination.totalPages }} ({{ pagination.totalElements }} usuários)
          </div>

          <div class="flex gap-2">
            <button
              @click="prevPage"
              :disabled="pagination.page === 0"
              :class="[
                'px-3 py-1 text-sm font-medium rounded-lg',
                pagination.page === 0
                  ? 'bg-gray-200 text-gray-400 cursor-not-allowed'
                  : 'bg-primary-600 text-white hover:bg-primary-700'
              ]"
            >
              Anterior
            </button>

            <button
              @click="nextPage"
              :disabled="pagination.page >= pagination.totalPages - 1"
              :class="[
                'px-3 py-1 text-sm font-medium rounded-lg',
                pagination.page >= pagination.totalPages - 1
                  ? 'bg-gray-200 text-gray-400 cursor-not-allowed'
                  : 'bg-primary-600 text-white hover:bg-primary-700'
              ]"
            >
              Próxima
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Edit User Modal -->
    <BaseModal :show="showEditModal" @close="closeEditModal" title="Editar Usuário">
      <form @submit.prevent="saveUser" class="space-y-4">
        <BaseInput
          v-model="editForm.name"
          type="text"
          label="Nome"
          placeholder="Nome do usuário"
          :error="errors.name"
          required
        />

        <BaseInput
          v-model="editForm.email"
          type="email"
          label="Email"
          placeholder="email@exemplo.com"
          :error="errors.email"
          required
        />

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Cargo *</label>
          <select
            v-model="editForm.role"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
          >
            <option v-for="roleOption in roles" :key="roleOption.value" :value="roleOption.value">
              {{ roleOption.label }}
            </option>
          </select>
        </div>

        <div class="flex justify-end space-x-2 pt-4">
          <button
            type="button"
            @click="closeEditModal"
            class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
          >
            Cancelar
          </button>
          <button
            type="submit"
            :disabled="loading"
            class="px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 disabled:bg-gray-400"
          >
            {{ loading ? 'Salvando...' : 'Salvar' }}
          </button>
        </div>
      </form>
    </BaseModal>

    <!-- Create User Modal -->
    <BaseModal :show="showCreateModal" @close="closeCreateModal" title="Novo Usuário">
      <form @submit.prevent="createUser" class="space-y-4">
        <BaseInput
          v-model="createForm.name"
          type="text"
          label="Nome"
          placeholder="Nome do usuário"
          :error="createErrors.name"
          required
        />

        <BaseInput
          v-model="createForm.email"
          type="email"
          label="Email"
          placeholder="email@exemplo.com"
          :error="createErrors.email"
          required
        />

        <BaseInput
          v-model="createForm.password"
          type="password"
          label="Senha"
          placeholder="Mínimo 8 caracteres"
          :error="createErrors.password"
          required
        />

        <div>
          <label class="block text-sm font-medium text-gray-700 mb-2">Cargo *</label>
          <select
            v-model="createForm.role"
            class="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-1 focus:ring-primary-500"
          >
            <option v-for="roleOption in roles" :key="roleOption.value" :value="roleOption.value">
              {{ roleOption.label }}
            </option>
          </select>
        </div>

        <div class="flex justify-end space-x-2 pt-4">
          <button
            type="button"
            @click="closeCreateModal"
            class="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-50"
          >
            Cancelar
          </button>
          <button
            type="submit"
            :disabled="loading"
            class="px-4 py-2 bg-primary-600 text-white rounded-md hover:bg-primary-700 disabled:bg-gray-400"
          >
            {{ loading ? 'Criando...' : 'Criar Usuário' }}
          </button>
        </div>
      </form>
    </BaseModal>
  </div>
</template>
