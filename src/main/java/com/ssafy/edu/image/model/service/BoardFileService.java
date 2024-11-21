package com.ssafy.edu.image.model.service;

import com.ssafy.edu.image.model.ImageBoardFile;
import com.ssafy.edu.image.model.mapper.ImageBoardFileMapper;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardFileService {

	private final ImageBoardFileMapper boardFileMapper;
	
    @Value("${file.upload.directory}")
    private String UPLOAD_DIR;
    
    private Path root ;
  
    public void init() {
      try {
		  root = Paths.get(UPLOAD_DIR);
	      if (!Files.exists(root)) {
	          Files.createDirectories(root);
	      }
      } catch (IOException e) {
        throw new RuntimeException("Could not initialize folder for upload!");
      }
    }
   

    @Transactional
    public List<ImageBoardFile> saveFiles(Long boardId, List<MultipartFile> files) throws IOException {
        List<ImageBoardFile> savedFiles = new ArrayList<>();
        
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String storedFilename = UUID.randomUUID().toString() + 
                    getFileExtension(originalFilename);
                
                Path targetPath = Paths.get(UPLOAD_DIR + "/"+storedFilename);
                Files.copy(file.getInputStream(), targetPath);

                ImageBoardFile boardFile = ImageBoardFile.builder()
                	    .boardId(boardId)
                	    .originalFilename(originalFilename)
                	    .storedFilename(storedFilename)
                	    .fileSize(file.getSize())
                	    .contentType(file.getContentType())
                	    .build();

                boardFileMapper.insert(boardFile);
                savedFiles.add(boardFile);
            }
        }
        
        return savedFiles;
    }

    @Transactional(readOnly = true)
    public ImageBoardFile getBoardFile(Long fileId) {
        ImageBoardFile boardFile = boardFileMapper.findById(fileId);
        if (boardFile == null) {
            throw new RuntimeException("파일을 찾을 수 없습니다.");
        }
        return boardFile;
    }

    @Transactional
    public void deleteFiles(List<Long> fileIds) {
        if (fileIds == null || fileIds.isEmpty()) return;

        List<String> storedFilenames = boardFileMapper.findByIds(fileIds)
            .stream()
            .map(ImageBoardFile::getStoredFilename)
            .toList();

        // 실제 파일 삭제
        for (String storedFilename : storedFilenames) {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_DIR +"/"+ storedFilename));
            } catch (IOException e) {
                // 로그 처리
            }
        }

        // DB에서 파일 정보 삭제
        boardFileMapper.deleteByIds(fileIds);
    }

    @Transactional
    public void deleteFilesByBoardId(Long boardId) {
        List<String> storedFilenames = boardFileMapper.findStoredFilenamesByBoardId(boardId);

        // 실제 파일 삭제
        for (String storedFilename : storedFilenames) {
            try {
                Files.deleteIfExists(Paths.get(UPLOAD_DIR +"/"+ storedFilename));
            } catch (IOException e) {
                // 로그 처리
            }
        }

        // DB에서 파일 정보 삭제
        boardFileMapper.deleteByBoardId(boardId);
    }

    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDotIndex = filename.lastIndexOf(".");
        return lastDotIndex > -1 ? filename.substring(lastDotIndex) : "";
    }
}