<template>
  <div class="relative group">
    <img 
      :src="image.imageUrl || useImageStore().getImageUrl(image.storedFilename)"
      :alt="image.originalFilename"
      class="w-full h-48 object-cover rounded-lg"
      @error="handleImageError"
    />
    <div class="mt-2 text-sm text-gray-500">
      {{ image.originalFilename }} ({{ formatFileSize(image.fileSize) }})
    </div>
  </div>
</template>

<script setup>
import { useImageStore } from '@/stores/image'

const props = defineProps({
  image: {
    type: Object,
    required: true
  }
})

const handleImageError = (event) => {
  event.target.src = '/assets/noimage.png'
}

const formatFileSize = (bytes) => {
  if (!bytes) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}
</script>