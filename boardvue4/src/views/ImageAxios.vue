
<!-- ImageAxiosView.vue -->
<template>
    <div class="container mx-auto px-4 py-8">
      <!-- 검색 및 업로드 영역 -->
      <div class="mb-8 flex flex-col md:flex-row gap-4">
        <div class="flex-grow bg-white p-4 rounded-lg shadow">
          <div class="flex flex-col md:flex-row gap-4">
            <select
              v-model="searchType"
              class="px-4 py-2 border rounded-md flex-1 md:flex-none"
            >
              <option value="title">제목</option>
              <option value="content">내용</option>
              <option value="authorName">작성자</option>
            </select>
            <input
              v-model="keyword"
              type="text"
              placeholder="검색어를 입력하세요"
              class="px-4 py-2 border rounded-md flex-grow"
              @keyup.enter="handleSearch"
            />
            <button
              @click="handleSearch"
              class="px-6 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600"
            >
              검색
            </button>
          </div>
        </div>
        
        <router-link
          to="/images/upload"
          class="flex items-center justify-center px-6 py-2 bg-white border rounded-lg shadow hover:bg-gray-50"
        >
          <svg class="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
          </svg>
          이미지 업로드
        </router-link>
      </div>
  
      <!-- 로딩 표시 -->
      <div v-if="loading" class="flex justify-center my-8">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"></div>
      </div>
  
      <!-- 이미지 그리드 -->
      <div v-else-if="imageList.length > 0" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        <router-link
          v-for="image in imageList"
          :key="image.id"
          :to="`/images/${image.id}`"
          class="block"
        >
          <div class="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg">
            <div class="p-4">
              <h3 class="font-medium text-gray-900 truncate">{{ image.title }}</h3>
              <p class="text-sm text-gray-500 mt-1 truncate">{{ image.content }}</p>
              <div class="mt-2 flex justify-between text-sm text-gray-500">
                <span>{{ image.authorName }}</span>
                <span>조회 {{ image.viewCount }}</span>
              </div>
              <div class="mt-1 text-xs text-gray-400">
                {{ formatDate(image.createdAt) }}
              </div>
            </div>
          </div>
        </router-link>
      </div>
      <div v-else class="text-center text-gray-500 py-8">
        등록된 게시글이 없습니다.
      </div>
  
      <!-- 페이지네이션 -->
      <div class="mt-8 flex flex-col md:flex-row justify-between items-center">
        <div class="mb-4 md:mb-0">
          <select 
            v-model="pageSize" 
            @change="handlePageSizeChange"
            class="px-3 py-2 border rounded-md"
          >
            <option :value="3">3개씩 보기</option>
            <option :value="5">5개씩 보기</option>
            <option :value="10">10개씩 보기</option>
          </select>
        </div>
  
        <div class="flex gap-2">
          <button 
            @click="changePage(pagination.prePage || 1)"
            :disabled="!pagination.hasPreviousPage"
            class="px-4 py-2 rounded-md border"
            :class="!pagination.hasPreviousPage ? 'bg-gray-100 text-gray-400' : 'hover:bg-gray-100'"
          >
            이전
          </button>
          
          <button 
            v-for="pageNum in pagination.navigatepageNums"
            :key="pageNum"
            @click="changePage(pageNum)"
            class="px-4 py-2 rounded-md border"
            :class="pagination.pageNum === pageNum ? 'bg-blue-500 text-white' : 'hover:bg-gray-100'"
          >
            {{ pageNum }}
          </button>
  
          <button 
            @click="changePage(pagination.nextPage || pagination.pages)"
            :disabled="!pagination.hasNextPage"
            class="px-4 py-2 rounded-md border"
            :class="!pagination.hasNextPage ? 'bg-gray-100 text-gray-400' : 'hover:bg-gray-100'"
          >
            다음
          </button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import axios from 'axios'
  
  const loading = ref(false)
  const searchType = ref('title')
  const keyword = ref('')
  const pageSize = ref(3)
  const imageList = ref([])
  const pagination = ref({
    total: 0,
    pageNum: 1,
    pageSize: 3,
    pages: 1,
    navigatepageNums: [],
    hasPreviousPage: false,
    hasNextPage: false,
    prePage: 0,
    nextPage: 0
  })
  
  const formatDate = (dateStr) => {
    if (!dateStr) return ''
    const date = new Date(dateStr)
    return new Intl.DateTimeFormat('ko-KR', {
      year: 'numeric',
      month: '2-digit',
      day: '2-digit',
      hour: '2-digit',
      minute: '2-digit'
    }).format(date)
  }
  
  const handleSearch = () => {
    fetchImages(1)
  }
  
  const handlePageSizeChange = () => {
    fetchImages(1)
  }
  
  const changePage = (page) => {
    fetchImages(page)
  }
  
  const fetchImages = async (page = 1) => {
    loading.value = true
    try {
      const response = await axios.get('/api/photos', {
        params: {
          page,
          size: pageSize.value,
          searchType: searchType.value,
          keyword: keyword.value
        }
      })
  
      imageList.value = response.data.list
      pagination.value = {
        total: response.data.total,
        pageNum: response.data.pageNum,
        pageSize: response.data.pageSize,
        pages: response.data.pages,
        navigatepageNums: response.data.navigatepageNums,
        hasPreviousPage: response.data.hasPreviousPage,
        hasNextPage: response.data.hasNextPage,
        prePage: response.data.prePage,
        nextPage: response.data.nextPage
      }
    } catch (error) {
      console.error('이미지 목록 로드 실패:', error)
    } finally {
      loading.value = false
    }
  }
  
  onMounted(() => {
    fetchImages()
  })
  </script>
  