<!-- src/views/Home.vue -->
<template>
  <div class="min-h-screen bg-gray-100">
    <NavBar/>
    <RouterView />
    <!-- 푸터 -->
    <footer class="bg-gray-800 text-white mt-12">
      <div class="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <div class="text-center">
          <p>&copy; 2024 Board System. All rights reserved.</p>
        </div>
      </div>
    </footer>
  </div>
</template>

<script setup>
import NavBar from '@/components/NavBar.vue'
</script>