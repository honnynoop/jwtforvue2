# boardvue04

This template should help get you started developing with Vue 3 in Vite.

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur).

## Customize configuration

See [Vite Configuration Reference](https://vite.dev/config/).

## Project Setup

```sh
npm install
```

### Compile and Hot-Reload for Development

```sh
npm run dev
```

### Compile and Minify for Production

```sh
npm run build
```


### Compile and Minify for Production
echo "# jwtforvue2" >> README.md
git init
git add README.md
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/honnynoop/jwtforvue2.git
git push -u origin main

v4를 만들고 branch 만들기 
# 원격 변경사항 가져오기
git fetch origin v4

# 원격 브랜치와 병합
git pull origin v4

# 충돌이 있다면 해결 후
git add .
git commit -m "Resolve conflicts"

# 강제 푸시 (주의: 기존 원격 내용이 덮어씌워짐)
git push -f origin v4

# 또는 안전한 방법:
git pull origin v4 --rebase
git push origin v4

### e:/upload/image
static 서버 문제 해결 업로드 후 반영안됨
pinia와 axios로 바로 사용 비교

페이징 파일업로드 이미지 서치 
E:\spring2024_1113\jwtforvue2
저장 

spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
#file.upload.directory=src/main/resources/static/assets/image
file.upload.directory=e:/upload/image/

MVC Config에 추가 file이 가능하도록 
	@Value("${file.upload.directory}")
		private String fileDir;

	   @Override
	   public void addResourceHandlers(ResourceHandlerRegistry registry) {
	       registry.addResourceHandler("/image/**") 
	               .addResourceLocations("file:" + fileDir); // 로컬 디렉토리 위치
	   }


![image](https://github.com/user-attachments/assets/7f9e8d95-5353-4386-a01c-78e02df9c400)
![image](https://github.com/user-attachments/assets/ddc73029-50d2-4bfc-aa3e-e9f1660f4600)
![image](https://github.com/user-attachments/assets/41f56c0a-e579-48e3-b6be-aa2feae46a29)
![image](https://github.com/user-attachments/assets/e1873979-4e33-46de-a644-82d64d926e5a)
![image](https://github.com/user-attachments/assets/8eb36210-217b-4a31-a37b-940d1e4ca333)
![image](https://github.com/user-attachments/assets/97880c9a-afed-43bf-b0c5-927aed76f14a)





