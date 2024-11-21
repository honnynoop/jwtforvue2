export function useImageUrl() {
    // 환경변수에서 URL을 가져오거나 기본값 사용
    const baseUrl = import.meta.env.VITE_API_URL || 'http://localhost:8080'
  
    const getImageUrl = (storedFilename) => {
      if (!storedFilename) return './assets/image/noimage.png'
      return `${baseUrl}/image/${storedFilename}`
    }
  
    return {
      getImageUrl
    }
  }