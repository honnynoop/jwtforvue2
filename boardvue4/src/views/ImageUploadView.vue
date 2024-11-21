<template>
  <div class="container mx-auto px-4 py-8">
    <div class="bg-white rounded-lg shadow-lg p-6">
      <!-- 헤더 -->
      <div class="flex justify-between items-center mb-6">
        <h2 class="text-2xl font-bold">이미지 게시글 작성</h2>
        <router-link
          to="/images"
          class="px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors"
        >
          목록으로
        </router-link>
      </div>

      <!-- 폼 -->
      <form @submit.prevent="handleSubmit" class="space-y-6">
        <!-- 제목 -->
        <div class="form-group">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >제목</label
          >
          <input
            v-model="formData.title"
            type="text"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            required
          />
        </div>

        <!-- 내용 -->
        <div class="form-group">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >내용</label
          >
          <textarea
            v-model="formData.content"
            rows="4"
            class="w-full px-4 py-2 border rounded-md focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            required
          ></textarea>
        </div>

        <!-- 파일 업로드 영역 -->
        <div class="form-group">
          <label class="block text-sm font-medium text-gray-700 mb-2"
            >파일 첨부</label
          >
          <div
            class="border-2 border-dashed border-gray-300 rounded-lg p-8"
            @drop.prevent="handleDrop"
            @dragover.prevent
            @dragenter.prevent
          >
            <div class="text-center">
              <input
                type="file"
                multiple
                accept="image/*"
                class="hidden"
                ref="fileInput"
                @change="handleFileSelect"
              />
              <button
                type="button"
                @click="$refs.fileInput.click()"
                class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors"
              >
                이미지 선택
              </button>
              <p class="mt-2 text-sm text-gray-500">
                또는 이미지를 여기에 드래그하여 업로드
              </p>
            </div>
          </div>

          <!-- 이미지 미리보기 -->
          <div v-if="previews.length > 0" class="mt-4">
            <h3 class="text-lg font-semibold mb-2">업로드 예정 이미지</h3>
            <div
              class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4"
            >
              <div
                v-for="(preview, index) in previews"
                :key="index"
                class="relative group"
              >
                <img
                  :src="preview.url"
                  :alt="preview.name"
                  class="w-full h-48 object-cover rounded-lg"
                />
                <button
                  @click.prevent="removePreview(index)"
                  class="absolute top-2 right-2 p-2 bg-red-500 text-white rounded-full opacity-0 group-hover:opacity-100 transition-opacity"
                >
                  <svg
                    class="w-4 h-4"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      stroke-linecap="round"
                      stroke-linejoin="round"
                      stroke-width="2"
                      d="M6 18L18 6M6 6l12 12"
                    />
                  </svg>
                </button>
                <div class="mt-1 text-sm text-gray-500">
                  {{ preview.name }} ({{ formatFileSize(preview.size) }})
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 제출 버튼 -->
        <div class="flex justify-end">
          <button
            type="submit"
            class="px-6 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors disabled:bg-gray-400"
            :disabled="loading || !isValid"
          >
            {{ loading ? "저장 중..." : "저장" }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onUnmounted } from "vue";
import { useRouter } from "vue-router";
import { useImageStore } from "@/stores/image";
import { useAuthStore } from "@/stores/auth";
import { storeToRefs } from "pinia";

const router = useRouter();
const imageStore = useImageStore();
const authStore = useAuthStore();
const { loading } = storeToRefs(imageStore);

const fileInput = ref(null);
const previews = ref([]);

// 폼 데이터
const formData = reactive({
  title: "",
  content: "",
});

// 유효성 검사
const isValid = computed(() => {
  const userId = parseInt(authStore.getMemberId, 10);
  return (
    formData.title.trim() &&
    formData.content.trim() &&
    userId &&
    previews.value.length > 0
  );
});

// 파일 크기 포맷팅
const formatFileSize = (bytes) => {
  if (!bytes) return "0 Bytes";
  const k = 1024;
  const sizes = ["Bytes", "KB", "MB", "GB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i];
};

// 파일 선택 처리
const handleFileSelect = (event) => {
  processFiles(Array.from(event.target.files));
  event.target.value = ""; // 동일 파일 재선택 가능하도록
};

// 드래그 앤 드롭 처리
const handleDrop = (event) => {
  processFiles(Array.from(event.dataTransfer.files));
};

// 파일 처리 공통 함수
const processFiles = (files) => {
  const imageFiles = files.filter((file) => file.type.startsWith("image/"));

  imageFiles.forEach((file) => {
    // 중복 체크
    const isDuplicate = previews.value.some(
      (preview) => preview.name === file.name && preview.size === file.size,
    );

    if (!isDuplicate) {
      previews.value.push({
        url: URL.createObjectURL(file),
        name: file.name,
        size: file.size,
        file: file,
      });
    }
  });
};

// 미리보기 제거
const removePreview = (index) => {
  URL.revokeObjectURL(previews.value[index].url);
  previews.value.splice(index, 1);
};

// 폼 제출
const handleSubmit = async () => {
  const userId = parseInt(authStore.getMemberId, 10);

  if (!userId) {
    alert("로그인이 필요합니다.");
    return;
  }

  try {
    const formDataToSend = new FormData();

    // 게시글 데이터 추가
    const boardData = {
      title: formData.title.trim(),
      content: formData.content.trim(),
      userId: userId,
    };

    formDataToSend.append(
      "board",
      new Blob([JSON.stringify(boardData)], {
        type: "application/json",
      }),
    );

    // 파일 데이터 추가
    previews.value.forEach((preview) => {
      formDataToSend.append("files", preview.file);
    });

    await imageStore.uploadImages();
    //await imageStore.fetchImage({page:1, size : 10, searchType:"", keyword:"" });
    alert("게시글이 성공적으로 등록되었습니다.");
    router.push("/images");
  } catch (error) {
    console.error("업로드 실패:", error);
    alert(error.response?.data?.message || "업로드에 실패했습니다.");
  }
};

// 컴포넌트 언마운트 시 URL 정리
onUnmounted(() => {
  previews.value.forEach((preview) => {
    URL.revokeObjectURL(preview.url);
  });
});
</script>
