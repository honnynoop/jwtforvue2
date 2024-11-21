<!-- ImageUploadAxios.vue -->
<template>
    <div class="container mx-auto px-4 py-8">
      <div class="bg-white rounded-lg shadow-lg p-6">
        <div class="flex justify-between items-center mb-6">
          <h2 class="text-2xl font-bold">이미지 게시글 작성</h2>
          <router-link to="/images" class="px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-50">
            목록으로
          </router-link>
        </div>
   
        <form @submit.prevent="handleSubmit" class="space-y-6">
          <div class="form-group">
            <label class="block text-sm font-medium text-gray-700 mb-2">제목</label>
            <input v-model="formData.title" type="text" class="w-full px-4 py-2 border rounded-md" required />
          </div>
   
          <div class="form-group">
            <label class="block text-sm font-medium text-gray-700 mb-2">내용</label>
            <textarea v-model="formData.content" rows="4" class="w-full px-4 py-2 border rounded-md" required></textarea>
          </div>
   
          <div class="form-group">
            <label class="block text-sm font-medium text-gray-700 mb-2">파일 첨부</label>
            <div class="border-2 border-dashed border-gray-300 rounded-lg p-8" @drop.prevent="handleDrop" @dragover.prevent>
              <div class="text-center">
                <input type="file" multiple accept="image/*" class="hidden" ref="fileInput" @change="handleFileSelect" />
                <button type="button" @click="$refs.fileInput.click()" class="px-4 py-2 bg-blue-500 text-white rounded-md">
                  이미지 선택
                </button>
                <p class="mt-2 text-sm text-gray-500">또는 이미지를 여기에 드래그하여 업로드</p>
              </div>
            </div>
   
            <div v-if="previews.length > 0" class="mt-4">
              <h3 class="text-lg font-semibold mb-2">업로드 예정 이미지</h3>
              <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
                <div v-for="(preview, index) in previews" :key="index" class="relative group">
                  <img :src="preview.url" :alt="preview.name" class="w-full h-48 object-cover rounded-lg" />
                  <button @click.prevent="removePreview(index)" class="absolute top-2 right-2 p-2 bg-red-500 text-white rounded-full opacity-0 group-hover:opacity-100">
                    <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                  </button>
                  <div class="mt-1 text-sm text-gray-500">{{ preview.name }} ({{ formatFileSize(preview.size) }})</div>
                </div>
              </div>
            </div>
          </div>
   
          <div class="flex justify-end">
            <button type="submit" class="px-6 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600" :disabled="loading">
              {{ loading ? "저장 중..." : "저장" }}
            </button>
          </div>
        </form>
      </div>
    </div>
   </template>
   
   <script setup>
   import { ref, reactive } from 'vue'
   import { useRouter } from 'vue-router'
   import axios from 'axios'
   import { useAuthStore } from "@/stores/auth";
   const authStore = useAuthStore();
   const router = useRouter()
   const loading = ref(false)
   const fileInput = ref(null)
   const previews = ref([])
   
   const formData = reactive({
    title: '',
    content: ''
   })
   
   const formatFileSize = (bytes) => {
    if (!bytes) return '0 Bytes'
    const k = 1024
    const sizes = ['Bytes', 'KB', 'MB', 'GB']
    const i = Math.floor(Math.log(bytes) / Math.log(k))
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
   }
   
   const handleFileSelect = (event) => {
    processFiles(Array.from(event.target.files))
    event.target.value = ''
   }
   
   const handleDrop = (event) => {
    processFiles(Array.from(event.dataTransfer.files))
   }
   
   const processFiles = (files) => {
    const imageFiles = files.filter(file => file.type.startsWith('image/'))
    
    imageFiles.forEach(file => {
      const isDuplicate = previews.value.some(preview => 
        preview.name === file.name && preview.size === file.size
      )
      
      if (!isDuplicate) {
        previews.value.push({
          url: URL.createObjectURL(file),
          name: file.name,
          size: file.size,
          file: file
        })
      }
    })
   }
   
   const removePreview = (index) => {
    URL.revokeObjectURL(previews.value[index].url)
    previews.value.splice(index, 1)
   }
   
   const handleSubmit = async () => {
    if (!previews.value.length) {
      alert('최소 1개의 이미지를 첨부해주세요.')
      return
    }
   
    loading.value = true
    
    try {
      const formDataToSend = new FormData()
      const userId = parseInt(authStore.getMemberId, 10);
      formDataToSend.append('board', new Blob([JSON.stringify({
        userId,
        title: formData.title.trim(),
        content: formData.content.trim()
      })], { type: 'application/json' }))
   
      previews.value.forEach(preview => {
        formDataToSend.append('files', preview.file)
      })
   
      await axios.post('/api/photos/upload', formDataToSend, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
   
      alert('게시글이 등록되었습니다.')
      router.push('/images')
    } catch (error) {
      console.error('업로드 실패:', error)
      alert(error.response?.data?.message || '업로드에 실패했습니다.')
    } finally {
      loading.value = false
    }
   }
   </script>