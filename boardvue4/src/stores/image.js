import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'

export const useImageStore = defineStore('image', () => {
  const loading = ref(false)
  const error = ref(null)
  const images = ref([])
  const baseUrl = import.meta.env.VITE_API_URL
  const pageInfo = ref({
    total: 0,
    currentPage: 1,
    size: 10,
    hasNext: false,
    hasPrevious: false
  })

  const getImageUrl = (filename) => {
    if (!filename) return '${baseUrl}/image/noimage.png'
    return `${baseUrl}/image/${filename}`
  }

  const fetchImages = async ({ page = 1, size = 10, searchType, keyword }) => {
    loading.value = true
    error.value = null

    try {
      const response = await axios.get('/api/photos', {
        params: { page, size, searchType, keyword }
      })
      images.value = response.data.list.map(image => ({
        ...image,
        imageUrl: getImageUrl(image.storedFilename)
      }))
      pageInfo.value = {
        total: response.data.total,
        currentPage: page,
        size: size,
        hasNext: response.data.hasNextPage,
        hasPrevious: page > 1
      }
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  const uploadImages = async (formData) => {
    loading.value = true
    error.value = null

    try {
      const response = await axios.post('/api/photos/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      await fetchImages({ page: 1, size: pageInfo.value.size })
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  const fetchImageDetail = async (id) => {
    loading.value = true
    error.value = null

    try {
      const response = await axios.get(`/api/photos/${id}`)
      if (response.data.files) {
        response.data.files = response.data.files.map(file => ({
          ...file,
          imageUrl: getImageUrl(file.storedFilename)
        }))
      }
      return response.data
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    error,
    images,
    pageInfo,
    getImageUrl,
    fetchImages,
    uploadImages,
    fetchImageDetail
  }
})