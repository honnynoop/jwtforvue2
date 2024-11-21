<template>
    <div class="bg-white rounded-lg shadow-md overflow-hidden group">
      <div class="relative">
        <button 
          @click.stop="$emit('delete', image.id)"
          class="absolute top-2 right-2 p-2 bg-red-500 text-white rounded-full opacity-0 group-hover:opacity-100 transition-opacity"
        >
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>
      <div class="p-4">
        <h3 class="font-medium text-gray-900 truncate">{{ image.title }}</h3>
        <p class="text-sm text-gray-500 mt-1">{{ image.writer }}</p>
        <div class="flex justify-between items-center mt-2 text-sm text-gray-500">
          <span>{{ formatDate(image.createdAt) }}</span>
          <span>{{ formatFileSize(image.fileSize) }}</span>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  const props = defineProps({
    image: {
      type: Object,
      required: true
    }
  })
  
  defineEmits(['delete'])
  
  const formatDate = (date) => {
    return new Date(date).toLocaleDateString()
  }
  
  const formatFileSize = (bytes) => {
    if (!bytes) return '0 Bytes'
    const k = 1024
    const sizes = ['Bytes', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
  }
  </script>