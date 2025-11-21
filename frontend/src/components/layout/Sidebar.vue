<script setup>
import { RouterLink, useRoute } from 'vue-router'
import { computed } from 'vue'

defineProps({
  isOpen: Boolean
})

const emit = defineEmits(['close'])

const route = useRoute()

const menuItems = [
  { name: 'Dashboard', path: '/', icon: 'chart' },
  { name: 'Produtos', path: '/produtos', icon: 'box' },
  { name: 'Serviços', path: '/servicos', icon: 'cog' },
  { name: 'Clientes', path: '/clientes', icon: 'users' },
  { name: 'Vendas', path: '/vendas', icon: 'shopping-cart' },
  { name: 'Usuários', path: '/usuarios', icon: 'user' },
  { name: 'Relatórios', path: '/relatorios', icon: 'document' }
]

const iconPaths = {
  chart: 'M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z',
  box: 'M20 7l-8-4-8 4m16 0l-8 4m8-4v10l-8 4m0-10L4 7m8 4v10M4 7v10l8 4',
  cog: 'M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z M15 12a3 3 0 11-6 0 3 3 0 016 0z',
  users: 'M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z',
  'shopping-cart': 'M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z',
  user: 'M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z',
  document: 'M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z'
}

function isActive(path) {
  return route.path === path
}
</script>

<template>
  <div class="h-screen">
    <div
      v-if="isOpen"
      @click="emit('close')"
      class="fixed inset-0 bg-gray-600 bg-opacity-50 z-20 lg:hidden"
    ></div>

    <aside
      :class="[
        'fixed lg:static inset-y-0 left-0 z-30 w-64 bg-white shadow-lg transform transition-transform duration-200 ease-in-out lg:h-full',
        isOpen ? 'translate-x-0' : '-translate-x-full lg:translate-x-0'
      ]"
    >
      <div class="flex items-center justify-center h-16 px-6 bg-primary-600 text-white">
        <h1 class="text-xl font-bold">Oficina</h1>
      </div>

      <nav class="mt-6">
        <RouterLink
          v-for="item in menuItems"
          :key="item.path"
          :to="item.path"
          @click="emit('close')"
          :class="[
            'flex items-center px-6 py-3 text-gray-700 hover:bg-primary-50 hover:text-primary-600 transition-colors',
            isActive(item.path) && 'bg-primary-50 text-primary-600 border-r-4 border-primary-600'
          ]"
        >
          <svg class="w-5 h-5 mr-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" :d="iconPaths[item.icon]"/>
          </svg>
          <span class="font-medium">{{ item.name }}</span>
        </RouterLink>
      </nav>
    </aside>
  </div>
</template>
