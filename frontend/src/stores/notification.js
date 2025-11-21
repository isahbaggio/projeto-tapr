import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useNotificationStore = defineStore('notification', () => {
  const notifications = ref([])
  let nextId = 0

  function notify(message, type = 'info', duration = 3000) {
    const id = nextId++
    const notification = {
      id,
      message,
      type,
      show: true
    }

    notifications.value.push(notification)

    setTimeout(() => {
      remove(id)
    }, duration)

    return id
  }

  function success(message, duration) {
    return notify(message, 'success', duration)
  }

  function error(message, duration) {
    return notify(message, 'error', duration)
  }

  function warning(message, duration) {
    return notify(message, 'warning', duration)
  }

  function info(message, duration) {
    return notify(message, 'info', duration)
  }

  function remove(id) {
    const index = notifications.value.findIndex(n => n.id === id)
    if (index !== -1) {
      notifications.value.splice(index, 1)
    }
  }

  return {
    notifications,
    notify,
    success,
    error,
    warning,
    info,
    remove
  }
})
