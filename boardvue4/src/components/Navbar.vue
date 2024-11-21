<template>
  <nav class="bg-white shadow-lg">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16">
        <div class="flex items-center">
          <router-link to="/" class="text-2xl font-bold text-indigo-600">
            Board System
          </router-link>
        </div>
        
        <div class="md:hidden flex items-center">
          <button @click="isOpen = !isOpen" class="text-gray-600 border border-transparent hover:border-blue-500 hover:text-blue-500 rounded-md transition-colors">
            <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path v-if="!isOpen" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
              <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        <div class="hidden md:flex items-center space-x-4">
          <template v-if="!authStore.isAuthenticated">
            <router-link 
              to="/login" 
              class="px-4 py-2 border border-gray-300 rounded-md hover:border-blue-500 hover:text-blue-500 transition-colors"
            >
              로그인
            </router-link>
            <router-link 
              to="/signup" 
              class="px-4 py-2 border border-gray-300 rounded-md hover:border-blue-500 hover:text-blue-500 transition-colors"
            >
              회원가입
            </router-link>
          </template>
          <template v-else>
            <router-link 
              to="/board" 
              class="px-4 py-2 border border-gray-300 rounded-md hover:border-blue-500 hover:text-blue-500 transition-colors"
            >
              게시판
            </router-link>
            <router-link 
              v-if="authStore.isAdmin" 
              to="/admin" 
              class="px-4 py-2 border border-gray-300 rounded-md hover:border-blue-500 hover:text-blue-500 transition-colors"
            >
              Admin
            </router-link>
            <router-link 
              to="/photo" 
              class="px-4 py-2 border border-gray-300 rounded-md hover:border-blue-500 hover:text-blue-500 transition-colors"
            >
              Photo
            </router-link>
            <button 
              @click="handleLogout" 
              class="px-4 py-2 border border-gray-300 rounded-md hover:border-blue-500 hover:text-blue-500 transition-colors"
            >
              로그아웃
            </button>
          </template>
        </div>
      </div>

      <div v-if="isOpen" class="md:hidden">
        <div class="px-2 pt-2 pb-3 space-y-1">
          <template v-if="!authStore.isAuthenticated">
            <router-link 
              to="/login" 
              class="block px-3 py-2 border border-gray-300 rounded-md hover:border-blue-500 hover:text-blue-500 transition-colors" 
              @click="isOpen = false"
            >
              로그인
            </router-link>
            <router-link 
              to="/signup" 
              class="block px-3 py-2 border border-gray-300 rounded-md hover:border-blue-500 hover:text-blue-500 transition-colors" 
              @click="isOpen = false"
            >
              회원가입
            </router-link>
          </template>
          <template v-else>
            <router-link 
              to="/board" 
              class="block px-3 py-2 border border-gray-300 rounded-md hover:border-blue-500 hover:text-blue-500 transition-colors" 
              @click="isOpen = false"
            >
              게시판
            </router-link>
            <router-link 
              v-if="authStore.isAdmin" 
              to="/admin" 
              class="block px-3 py-2 border border-gray-300 rounded-md hover:border-blue-500 hover:text-blue-500 transition-colors" 
              @click="isOpen = false"
            >
              Admin
            </router-link>
            <router-link 
              to="/photo" 
              class="block px-3 py-2 border border-gray-300 rounded-md hover:border-blue-500 hover:text-blue-500 transition-colors" 
              @click="isOpen = false"
            >
              Photo
            </router-link>
            <button 
              @click="handleLogout" 
              class="w-full text-left px-3 py-2 border border-gray-300 rounded-md hover:border-blue-500 hover:text-blue-500 transition-colors"
            >
              로그아웃
            </button>
          </template>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const router = useRouter()
const authStore = useAuthStore()
const isOpen = ref(false)

const handleLogout = async () => {
  try {
    await authStore.logout()
    isOpen.value = false
    router.push('/login')
  } catch (error) {
    console.error('로그아웃 중 오류 발생:', error)
    alert('로그아웃 중 오류가 발생했습니다.')
  }
}
</script>