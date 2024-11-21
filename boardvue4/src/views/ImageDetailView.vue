<template>
  <div class="container mx-auto px-4 py-8">
    <div class="bg-white rounded-lg shadow-lg">
      <!-- 헤더 영역 -->
      <div class="p-6 border-b">
        <div class="flex justify-between items-center mb-4">
          <h1 class="text-2xl font-bold">{{ image?.title }}</h1>
          <router-link
            to="/images"
            class="px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors"
          >
            목록으로
          </router-link>
        </div>
        <div class="flex justify-between items-center text-sm text-gray-500">
          <div class="flex items-center gap-4">
            <span>작성자: {{ image?.authorName }}</span>
            <span>조회수: {{ image?.viewCount }}</span>
          </div>
          <div class="flex items-center gap-4">
            <span>작성일: {{ formatDate(image?.createdAt) }}</span>
            <span v-if="image?.updatedAt !== image?.createdAt">
              수정일: {{ formatDate(image?.updatedAt) }}
            </span>
          </div>
        </div>
      </div>

      <!-- 내용 영역 -->
      <div class="p-6 border-b">
        <p class="whitespace-pre-wrap">{{ image?.content }}</p>
      </div>

      <!-- 이미지 영역 -->
      <div class="p-6">
        <h2 class="text-lg font-semibold mb-4">첨부 이미지</h2>
        <div v-if="image?.files?.length" class="grid grid-cols-1 gap-6">
          <div
            v-for="file in image.files"
            :key="file.id"
            class="flex flex-col items-center"
          >
            <img
              :src="getImageUrl(file.storedFilename)"
              :alt="file.originalFilename"
              class="max-w-full rounded-lg shadow-lg"
              loading="lazy"
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

      <!-- 하단 버튼 영역 -->
      <div class="p-6 border-t flex justify-end gap-4" v-if="isAuthor">
        <button
          @click="handleEdit"
          class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors"
        >
          수정
        </button>
        <button
          @click="handleDelete"
          class="px-4 py-2 bg-red-500 text-white rounded-md hover:bg-red-600 transition-colors"
        >
          삭제
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from "@/stores/auth";
import { useImageStore } from "@/stores/image";
import { storeToRefs } from "pinia";
import { computed, onMounted, ref } from "vue";
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const imageStore = useImageStore();
const authStore = useAuthStore();
const { loading } = storeToRefs(imageStore);

const image = ref(null);

// 작성자 확인
const isAuthor = computed(() => {
  const currentUserId = parseInt(authStore.getMemberId, 10);
  return image.value?.userId === currentUserId;
});

// 날짜 포맷팅
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

// 파일 크기 포맷팅
const formatFileSize = (bytes) => {
  if (!bytes) return "0 Bytes";
  const k = 1024;
  const sizes = ["Bytes", "KB", "MB", "GB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i];
};

// 이미지 URL 생성
const getImageUrl = (filename) => {
  if (!filename) return `./assets/image/noimage.png`;
  return `${import.meta.env.VITE_API_URL || ""}/image/${filename}`;
};

// 이미지 로드 에러 처리
const handleImageError = (event) => {
  event.target.src = getImageUrl("noimage.png");
};

// 게시글 수정
const handleEdit = () => {
  router.push(`/images/edit/${image.value.id}`);
};

// 게시글 삭제
const handleDelete = async () => {
  if (!confirm("정말 삭제하시겠습니까?")) return;

  try {
    await imageStore.deleteImage(image.value.id);
    alert("게시글이 삭제되었습니다.");
    router.push("/images");
  } catch (error) {
    console.error("삭제 실패:", error);
    alert("삭제에 실패했습니다.");
  }
};

// 데이터 로드
onMounted(async () => {
  try {
    const imageId = route.params.id;
    const response = await imageStore.fetchImageDetail(imageId);
    image.value = response;
  } catch (error) {
    console.error("이미지 상세 정보 로드 실패:", error);
    alert("게시글을 불러오는데 실패했습니다.");
    router.push("/images");
  }
});
</script>
