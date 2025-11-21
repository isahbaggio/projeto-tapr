<script setup>
import { useNotificationStore } from '@/stores/notification'
import NotificationToast from './NotificationToast.vue'

const notificationStore = useNotificationStore()
</script>

<template>
  <div class="fixed top-4 right-4 z-50 space-y-2">
    <TransitionGroup name="notification">
      <NotificationToast
        v-for="notification in notificationStore.notifications"
        :key="notification.id"
        :notification="notification"
        @close="notificationStore.remove(notification.id)"
      />
    </TransitionGroup>
  </div>
</template>

<style scoped>
.notification-enter-active,
.notification-leave-active {
  transition: all 0.3s ease;
}

.notification-enter-from {
  transform: translateX(100%);
  opacity: 0;
}

.notification-leave-to {
  transform: translateX(100%);
  opacity: 0;
}
</style>
