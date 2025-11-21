<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useNotificationStore } from '@/stores/notification'
import BaseInput from '@/components/ui/BaseInput.vue'
import BaseButton from '@/components/ui/BaseButton.vue'

const router = useRouter()
const authStore = useAuthStore()
const notification = useNotificationStore()

const email = ref('')
const password = ref('')
const errors = ref({})
const isLoading = ref(false)

function validateForm() {
  errors.value = {}

  if (!email.value) {
    errors.value.email = 'Email é obrigatório'
  } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
    errors.value.email = 'Email inválido'
  }

  if (!password.value) {
    errors.value.password = 'Senha é obrigatória'
  } else if (password.value.length < 8) {
    errors.value.password = 'Senha deve ter no mínimo 8 caracteres'
  }

  return Object.keys(errors.value).length === 0
}

async function handleLogin() {
  if (!validateForm()) return

  try {
    isLoading.value = true
    await authStore.login(email.value, password.value)
    notification.success('Login realizado com sucesso')
    router.push('/')
  } catch (error) {
    notification.error(error.response?.data?.message || 'Erro ao fazer login')
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary-50 to-primary-100 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div class="bg-white rounded-2xl shadow-xl p-8">
        <div class="text-center mb-8">
          <h1 class="text-3xl font-bold text-gray-900 mb-2">Oficina</h1>
          <p class="text-sm text-gray-600">Sistema de Gestão</p>
        </div>

        <h2 class="text-2xl font-semibold text-gray-900 mb-6">Login</h2>

        <form @submit.prevent="handleLogin" class="space-y-4">
          <BaseInput
            v-model="email"
            type="email"
            label="Email"
            placeholder="seu@email.com"
            :error="errors.email"
            required
          />

          <BaseInput
            v-model="password"
            type="password"
            label="Senha"
            placeholder="********"
            :error="errors.password"
            required
          />

          <BaseButton
            type="submit"
            variant="primary"
            size="lg"
            :loading="isLoading"
            class="w-full"
          >
            Entrar
          </BaseButton>
        </form>

        <div class="mt-6 text-center">
          <p class="text-sm text-gray-600">
            Não tem uma conta?
            <RouterLink to="/register" class="text-primary-600 hover:text-primary-700 font-medium">
              Registre-se
            </RouterLink>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>
