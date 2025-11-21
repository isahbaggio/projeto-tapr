<script setup>
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import { ref } from 'vue'

const emit = defineEmits(['toggleSidebar'])

const authStore = useAuthStore()
const router = useRouter()
const showUserMenu = ref(false)

function handleLogout() {
  authStore.logout()
  router.push('/login')
}
</script>

<template>
  <header class="bg-white shadow-sm">
    <div class="flex items-center justify-between px-6 py-4">
      <button
        @click="emit('toggleSidebar')"
        class="lg:hidden text-gray-600 hover:text-gray-900 focus:outline-none"
      >
        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
        </svg>
      </button>

      <div class="flex-1"></div>

      <div class="relative">
        <button
          @click="showUserMenu = !showUserMenu"
          class="flex items-center space-x-3 focus:outline-none"
        >
          <div class="w-8 h-8 bg-primary-600 text-white rounded-full flex items-center justify-center">
            <span class="text-sm font-semibold">{{ authStore.user?.email?.charAt(0).toUpperCase() }}</span>
          </div>
          <div class="hidden md:block text-left">
            <p class="text-sm font-medium text-gray-700">{{ authStore.user?.email }}</p>
            <p class="text-xs text-gray-500">{{ authStore.user?.role }}</p>
          </div>
        </button>

        <div
          v-if="showUserMenu"
          @click.stop
          class="absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg py-1 z-50"
        >
          <button
            @click="handleLogout"
            class="block w-full text-left px-4 py-2 text-sm text-gray-700 hover:bg-gray-100"
          >
            Sair
          </button>
        </div>
      </div>
    </div>
  </header>

  <div v-if="showUserMenu" @click="showUserMenu = false" class="fixed inset-0 z-40"></div>
</template>
