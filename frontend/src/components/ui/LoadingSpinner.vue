<script setup>
defineProps({
  size: {
    type: String,
    default: 'md',
    validator: (value) => ['sm', 'md', 'lg'].includes(value)
  }
})

const sizeClasses = {
  sm: 'w-4 h-4',
  md: 'w-8 h-8',
  lg: 'w-12 h-12'
}
</script>

<template>
  <div class="flex justify-center items-center">
    <div
      :class="[
        'animate-spin rounded-full border-4 border-gray-200 border-t-primary-600',
        sizeClasses[size]
      ]"
    ></div>
  </div>
</template>
