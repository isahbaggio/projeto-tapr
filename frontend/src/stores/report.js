import { defineStore } from 'pinia'
import reportService from '@/services/report.service'
import { useNotificationStore } from './notification'

export const useReportStore = defineStore('report', {
  state: () => ({
    produtoReport: null,
    servicoReport: null,
    clienteReport: null,
    systemOverview: null,
    loading: false
  }),

  actions: {
    async fetchProdutoReport() {
      this.loading = true
      try {
        const response = await reportService.getProdutoReport()
        this.produtoReport = response.data
      } catch (error) {
        const notificationStore = useNotificationStore()
        notificationStore.addNotification('Erro ao carregar relatorio de produtos', 'error')
      } finally {
        this.loading = false
      }
    },

    async fetchServicoReport() {
      this.loading = true
      try {
        const response = await reportService.getServicoReport()
        this.servicoReport = response.data
      } catch (error) {
        const notificationStore = useNotificationStore()
        notificationStore.addNotification('Erro ao carregar relatorio de servicos', 'error')
      } finally {
        this.loading = false
      }
    },

    async fetchClienteReport() {
      this.loading = true
      try {
        const response = await reportService.getClienteReport()
        this.clienteReport = response.data
      } catch (error) {
        const notificationStore = useNotificationStore()
        notificationStore.addNotification('Erro ao carregar relatorio de clientes', 'error')
      } finally {
        this.loading = false
      }
    },

    async fetchSystemOverview() {
      this.loading = true
      try {
        const response = await reportService.getSystemOverview()
        this.systemOverview = response.data
      } catch (error) {
        const notificationStore = useNotificationStore()
        notificationStore.addNotification('Erro ao carregar visao geral do sistema', 'error')
      } finally {
        this.loading = false
      }
    },

    async fetchAllReports() {
      await Promise.all([
        this.fetchProdutoReport(),
        this.fetchServicoReport(),
        this.fetchClienteReport(),
        this.fetchSystemOverview()
      ])
    }
  }
})
