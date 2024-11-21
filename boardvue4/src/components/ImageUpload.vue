<template>
  <div class="container mx-auto px-4 py-8">
    <div class="mb-8">
      <h2 class="text-2xl font-bold mb-4">이미지 업로드</h2>
      <div
        class="border-2 border-dashed border-gray-300 rounded-lg p-8 text-center"
      >
        <input
          type="file"
          multiple
          accept="image/*"
          @change="handleFileSelect"
          class="hidden"
          ref="fileInput"
        />
        <button
          @click="$refs.fileInput.click()"
          class="px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 transition-colors"
        >
          이미지 선택
        </button>
        <p class="mt-2 text-sm text-gray-500">
          또는 이미지를 여기에 드래그하여 업로드
        </p>
      </div>

      <!-- 미리보기 영역 -->
      <div v-if="previews.length > 0" class="mt-4">
        <h3 class="text-lg font-semibold mb-2">업로드 예정 이미지</h3>
        <div
          class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4"
        >
          <ImageComponent
            v-for="(preview, index) in previews"
            :key="index"
            :image="preview"
            :showDeleteButton="true"
            @delete="removePreview(index)"
          />
        </div>
        <div class="mt-4">
          <button
            @click="uploadImages"
            class="px-4 py-2 bg-green-500 text-white rounded-md hover:bg-green-600 transition-colors"
            :disabled="uploading"
          >
            {{ uploading ? "업로드 중..." : "이미지 업로드" }}
          </button>
        </div>
      </div>
    </div>

    <!-- 이미지 목록 -->
    <div>
      <h2 class="text-2xl font-bold mb-4">이미지 목록</h2>
      <div
        class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4"
      >
        <ImageComponent
          v-for="image in images"
          :key="image.id"
          :image="image"
          @delete="deleteImage(image.id)"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useImageStore } from "@/stores/image";
import ImageComponent from "@/components/ImageComponent.vue";
import axios from "axios";

const imageStore = useImageStore();
const fileInput = ref(null);
const previews = ref([]);
const uploading = ref(false);
const images = ref([]);

// 이미지 선택 처리
const handleFileSelect = (event) => {
  const files = event.target.files;
  Array.from(files).forEach((file) => {
    if (file.type.startsWith("image/")) {
      const reader = new FileReader();
      reader.onload = (e) => {
        previews.value.push({
          url: e.target.result,
          name: file.name,
          size: formatFileSize(file.size),
          file: file,
        });
      };
      reader.readAsDataURL(file);
    }
  });
};

// 파일 크기 포맷
const formatFileSize = (bytes) => {
  if (bytes === 0) return "0 Bytes";
  const k = 1024;
  const sizes = ["Bytes", "KB", "MB", "GB"];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i];
};

// 미리보기 제거
const removePreview = (index) => {
  previews.value.splice(index, 1);
};

// 이미지 업로드
const uploadImages = async () => {
  try {
    uploading.value = true;
    const formData = new FormData();
    previews.value.forEach((preview) => {
      formData.append("files", preview.file);
    });

    await axios.post("/api/photos/upload", formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });

    // 업로드 성공 후 목록 새로고침
    await fetchImages();
    previews.value = []; // 미리보기 초기화
  } catch (error) {
    console.error("이미지 업로드 실패:", error);
  } finally {
    uploading.value = false;
  }
};

// 이미지 목록 조회
const fetchImages = async () => {
  try {
    const response = await axios.get("/api/photos");
    images.value = response.data;
  } catch (error) {
    console.error("이미지 목록 조회 실패:", error);
  }
};

// 이미지 삭제
const deleteImage = async (imageId) => {
  try {
    await axios.delete(`/api/photos/${imageId}`);
    await fetchImages(); // 목록 새로고침
  } catch (error) {
    console.error("이미지 삭제 실패:", error);
  }
};

onMounted(() => {
  fetchImages();
});
</script>
