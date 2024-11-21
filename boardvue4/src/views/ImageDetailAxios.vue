<!-- ImageDetailAxios.vue -->
<template>
  <div class="container mx-auto px-4 py-8">
    <div class="bg-white rounded-lg shadow-lg">
      <div class="p-6 border-b">
        <div class="flex justify-between items-center mb-4">
          <h1 class="text-2xl font-bold">{{ image?.title }}</h1>
          <router-link
            to="/images"
            class="px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-50"
          >
            목록으로
          </router-link>
        </div>
        <div class="flex justify-between items-center text-sm text-gray-500">
          <div class="flex items-center gap-4">
            <span>작성자: {{ image?.authorName }}</span>
            <span>조회수: {{ image?.viewCount }}</span>
          </div>
          <div>작성일: {{ formatDate(image?.createdAt) }}</div>
        </div>
      </div>

      <div class="p-6 border-b">
        <p class="whitespace-pre-wrap">{{ image?.content }}</p>
      </div>

      <div class="p-6">
        <h2 class="text-lg font-semibold mb-4">첨부 이미지</h2>
        <div v-if="image?.files?.length" class="grid grid-cols-1 gap-6">
          <div
            v-for="file in image.files"
            :key="file.id"
            class="flex flex-col items-center"
          >
            <img
              :src="file.storedFilename"
              :alt="file.originalFilename"
              class="max-w-full rounded-lg shadow-lg"
              @error="handleImageError"
            />
            <div class="mt-2 text-sm text-gray-500">
              {{ file.originalFilename }} ({{ formatFileSize(file.fileSize) }})
            </div>
          </div>
        </div>
        <div v-else class="text-center text-gray-500 py-4">
          첨부된 이미지가 없습니다.
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import { onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const image = ref(null);
const baseUrl = import.meta.env.VITE_API_URL;

const getImageUrl = (filename) => {
  //alert(filename);
  if (!filename) return "/assets/image/noimage.png";
  else return `${baseUrl}/image/${filename}`;
};

const handleImageError = (event) => {
  event.target.src = getImageUrl("noimage.png");
};

const formatDate = (dateStr) => {
  if (!dateStr) return "";
  const date = new Date(dateStr);
  return new Intl.DateTimeFormat("ko-KR", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  }).format(date);
};

const formatFileSize = (bytes) => {
  if (!bytes) return "0 Bytes";
  const k = 1024;
  const sizes = ["Bytes", "KB", "MB", "GB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i];
};

onMounted(async () => {
  alert(`${route.params.id}`);
  try {
    const response = await axios.get(`/api/photos/${route.params.id}`);
    if (response.data.files) {
      response.data.files = response.data.files.map((file) => ({
        ...file,
        storedFilename: getImageUrl(file.storedFilename),
      }));
    }
    image.value = response.data;
  } catch (error) {
    console.error("이미지 상세 정보 로드 실패:", error);
    alert("게시글을 불러오는데 실패했습니다.");
    router.push("/images");
  }
});
</script>
