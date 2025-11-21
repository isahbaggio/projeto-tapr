<script setup>
const props = defineProps({
  notification: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['close'])

const typeStyles = {
  success: 'bg-green-50 border-green-500 text-green-800',
  error: 'bg-red-50 border-red-500 text-red-800',
  warning: 'bg-yellow-50 border-yellow-500 text-yellow-800',
  info: 'bg-blue-50 border-blue-500 text-blue-800'
}

const typeIcons = {
  success: '✓',
  error: '✕',
  warning: '⚠',
  info: 'ℹ'
}
</script>

<template>
  <div
    :class="[
      'flex items-center gap-3 min-w-80 max-w-md p-4 rounded-lg border-l-4 shadow-lg',
      typeStyles[notification.type] || typeStyles.info
    ]"
  >
    <div class="flex-shrink-0 w-6 h-6 flex items-center justify-center font-bold text-lg">
      {{ typeIcons[notification.type] || typeIcons.info }}
    </div>

    <div class="flex-1">
      <p class="text-sm font-medium">{{ notification.message }}</p>
    </div>

    <button
      @click="emit('close')"
      class="flex-shrink-0 text-gray-400 hover:text-gray-600"
    >
      <svg class="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
        <path
          fill-rule="evenodd"
          d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
          clip-rule="evenodd"
        />
      </svg>
    </button>
  </div>
</template>
