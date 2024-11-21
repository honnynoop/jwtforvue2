<template>
  <main class="max-w-7xl mx-auto py-12 px-4 sm:px-6 lg:px-8">
    <div class="container mx-auto px-4 py-8">
      <div
        class="flex flex-col sm:flex-row justify-between items-center mb-6 gap-4"
      >
        <h1 class="text-2xl font-bold">게시판</h1>
        <router-link
          to="/board/write"
          class="w-full sm:w-auto px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 text-center"
        >
          글쓰기
        </router-link>
      </div>

      <div class="mb-6 flex flex-col sm:flex-row gap-4">
        <select
          v-model="searchKeyword"
          class="w-full sm:w-auto px-3 py-2 border rounded"
        >
          <option value="title">제목</option>
          <option value="content">내용</option>
        </select>
        <input
          v-model="searchText"
          type="text"
          class="w-full sm:flex-1 px-3 py-2 border rounded"
          placeholder="검색어를 입력하세요"
        />
        <button
          @click="handleSearch"
          class="w-full sm:w-auto px-4 py-2 bg-gray-500 text-white rounded hover:bg-gray-600"
        >
          검색
        </button>
      </div>

      <!-- PC 화면 테이블 -->
      <div class="hidden sm:block overflow-x-auto bg-white shadow rounded-lg">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th
                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                번호
              </th>
              <th
                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                제목
              </th>
              <th
                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                작성일
              </th>
              <th
                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                조회수
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="board in boards" :key="board.boardId">
              <td class="px-6 py-4 whitespace-nowrap text-sm">
                {{ board.boardId }}
              </td>
              <td class="px-6 py-4 text-sm">{{ board.title }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm">
                {{ formatDate(board.writedate) }}
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm">
                {{ board.hit }}
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- 모바일 카드 뷰 -->
      <div class="sm:hidden space-y-2">
        <div
          v-for="board in boards"
          :key="board.boardId"
          class="bg-white shadow rounded-lg p-4"
        >
          <div class="flex items-center gap-3">
            <span class="text-gray-600 font-medium">{{ board.boardId }}</span>
            <h3 class="flex-1 font-medium">{{ board.title }}</h3>
          </div>
          <div class="mt-2 flex text-sm text-gray-500 gap-4">
            <span>{{ formatDate(board.writedate) }}</span>
            <span>조회 {{ board.hit }}</span>
          </div>
        </div>
      </div>

      <div class="mt-6 flex flex-wrap justify-center gap-2">
        <button
          v-for="page in pages"
          :key="page"
          @click="changePage(page)"
          :class="[
            'px-3 py-1 rounded text-sm',
            currentPage === page
              ? 'bg-blue-500 text-white'
              : 'bg-gray-200 hover:bg-gray-300',
          ]"
        >
          {{ page }}
        </button>
      </div>
    </div>
  </main>
</template>
<script setup>
import { useBoardStore } from "@/stores/board";
import { computed, onMounted, ref } from "vue";

const boardStore = useBoardStore();
const searchKeyword = ref("title");
const searchText = ref("");

const boards = computed(() => boardStore.boards);
const currentPage = computed(() => boardStore.currentPage);
const pages = computed(() => {
  const pageCount = Math.ceil(boardStore.total / boardStore.pageSize);
  return Array.from({ length: pageCount }, (_, i) => i + 1);
});

const handleSearch = () => {
  boardStore.setSearch(searchKeyword.value, searchText.value);
  boardStore.fetchBoards();
};

const changePage = (page) => {
  boardStore.setPage(page);
  boardStore.fetchBoards();
};

const formatDate = (date) => {
  return new Date(date).toLocaleDateString();
};

onMounted(() => {
  boardStore.fetchBoards();
});
</script>
