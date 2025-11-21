<template>
  <div class="p-6">
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-900">Nova Venda</h1>
      <p class="text-gray-600 mt-1">Crie uma nova venda seguindo as etapas</p>
    </div>

    <VendaWizard
      @submit="criarVenda"
      @cancel="voltarParaListagem"
    />
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { useVendasStore } from '@/stores/vendas'
import VendaWizard from '@/components/vendas/VendaWizard.vue'

export default {
  name: 'NovaVendaView',
  components: {
    VendaWizard
  },
  setup() {
    const router = useRouter()
    const vendasStore = useVendasStore()

    async function criarVenda(vendaData) {
      try {
        await vendasStore.createVenda(vendaData)
        router.push('/vendas')
      } catch (error) {
        console.error('Erro ao criar venda:', error)
      }
    }

    function voltarParaListagem() {
      router.push('/vendas')
    }

    return {
      criarVenda,
      voltarParaListagem
    }
  }
}
</script>
