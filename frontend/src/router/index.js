import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/auth/LoginView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      component: () => import('@/components/layout/AppLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          name: 'dashboard',
          component: () => import('@/views/dashboard/DashboardView.vue')
        },
        {
          path: 'produtos',
          name: 'produtos',
          component: () => import('@/views/produtos/ProdutosView.vue')
        },
        {
          path: 'servicos',
          name: 'servicos',
          component: () => import('@/views/servicos/ServicosView.vue')
        },
        {
          path: 'clientes',
          name: 'clientes',
          component: () => import('@/views/clientes/ClientesView.vue')
        },
        {
          path: 'vendas',
          name: 'vendas',
          component: () => import('@/views/vendas/VendasView.vue')
        },
        {
          path: 'vendas/nova',
          name: 'nova-venda',
          component: () => import('@/views/vendas/NovaVendaView.vue')
        },
        {
          path: 'usuarios',
          name: 'usuarios',
          component: () => import('@/views/users/UsersView.vue')
        },
        {
          path: 'relatorios',
          name: 'relatorios',
          component: () => import('@/views/ReportsView.vue')
        }
      ]
    }
  ]
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()

  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next({ name: 'login' })
  } else if (!to.meta.requiresAuth && authStore.isAuthenticated && to.name !== 'dashboard') {
    next({ name: 'dashboard' })
  } else {
    next()
  }
})

export default router
