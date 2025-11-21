<script setup>
import { ref, onMounted } from 'vue'
import { userService } from '@/services/user.service'
import { useNotificationStore } from '@/stores/notification'
import LoadingSpinner from '@/components/ui/LoadingSpinner.vue'
import EmptyState from '@/components/common/EmptyState.vue'

const notification = useNotificationStore()

const users = ref([])
const loading = ref(false)
const pagination = ref({
  page: 0,
  size: 10,
  totalPages: 0,
  totalElements: 0
})

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

onMounted(() => {
  fetchUsers()
})
</script>

<template>
  <div>
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-900">Usuários</h1>
      <p class="text-gray-600">Gerenciar usuários do sistema</p>
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
                Role
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
                    user.role === 'ADMIN' ? 'bg-purple-100 text-purple-800' :
                    user.role === 'MANAGER' ? 'bg-blue-100 text-blue-800' :
                    user.role === 'WAITER' ? 'bg-green-100 text-green-800' :
                    'bg-gray-100 text-gray-800'
                  ]"
                >
                  {{ user.role }}
                </span>
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
  </div>
</template>
