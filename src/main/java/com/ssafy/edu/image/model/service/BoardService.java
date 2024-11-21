package com.ssafy.edu.image.model.service;

import com.ssafy.edu.common.util.FileUploadUtil;
import com.ssafy.edu.image.model.ImageBoard;
import com.ssafy.edu.image.model.ImageBoardFile;
import com.ssafy.edu.image.model.mapper.ImageBoardFileMapper;
import com.ssafy.edu.image.model.mapper.ImageBoardMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BoardService {

    private final ImageBoardMapper boardMapper;
    private final ImageBoardFileMapper boardFileMapper;
    private final FileUploadUtil fileUploadUtil;

    @Transactional
    public ImageBoard createBoard(ImageBoard board, List<MultipartFile> files) {
    	log.debug("1------------------------->{}",board);
        boardMapper.insert(board);
        log.debug("2------------------------->{}",board);
        if (files != null && !files.isEmpty()) {
            try {
                List<String> uploadedFiles = fileUploadUtil.uploadFiles(files);
                for (int i = 0; i < files.size(); i++) {
                    ImageBoardFile boardFile = new ImageBoardFile();
                    boardFile.setBoardId(board.getId());// 자동 삽입됨
                    boardFile.setOriginalFilename(files.get(i).getOriginalFilename());
                    boardFile.setStoredFilename(uploadedFiles.get(i));
                    boardFile.setFileSize(files.get(i).getSize());
                    boardFile.setContentType(files.get(i).getContentType());
                    boardFileMapper.insert(boardFile);
                }
            } catch (Exception e) {
                throw new RuntimeException("파일 업로드 실패", e);
            }
        }

        return getBoardWithFiles(board.getId());
    }

    @Transactional
    public ImageBoard getBoardWithFiles(Long id) {
    	log.debug("id=======================>{}",id);
        ImageBoard board = boardMapper.findById(id);
        log.debug("board=======================>{}",board);
        if (board != null) {
            List<ImageBoardFile> files = boardFileMapper.findByBoardId(id);
            board.setFiles(files);
            log.debug("board=======================>{}",board);
            boardMapper.incrementViewCount(id);
        }
        return board;
    }
    @Transactional
    public ImageBoard getBoardFileDetail(Long id) {
    	log.debug("id=======================>{}",id);
        ImageBoard board = boardMapper.findById(id);
        log.debug("board=======================>{}",board);
        if (board != null) {
            List<ImageBoardFile> files = boardFileMapper.findByBoardId(id);
            board.setFiles(files);
            //boardMapper.incrementViewCount(id);
        }
        return board;
    }
    @Transactional(readOnly = true)
    public PageInfo<ImageBoard> getBoards(int page, int size, String searchType, String keyword) {
        PageHelper.startPage(page, size);
        List<ImageBoard> boards = boardMapper.findAll(searchType, keyword);
        return new PageInfo<>(boards);
    }

    @Transactional
    public ImageBoard updateBoard(ImageBoard board, List<MultipartFile> newFiles, List<Long> deleteFileIds) {
        boardMapper.update(board);

        // 파일 삭제 처리
        if (deleteFileIds != null && !deleteFileIds.isEmpty()) {
            for (Long fileId : deleteFileIds) {
                ImageBoardFile file = boardFileMapper.findById(fileId);
                if (file != null) {
                    fileUploadUtil.deleteFile(file.getStoredFilename());
                    boardFileMapper.deleteByBoardId(fileId);
                }
            }
        }

        // 새 파일 업로드 처리
        if (newFiles != null && !newFiles.isEmpty()) {
            try {
                List<String> uploadedFiles = fileUploadUtil.uploadFiles(newFiles);
                for (int i = 0; i < newFiles.size(); i++) {
                    ImageBoardFile boardFile = new ImageBoardFile();
                    boardFile.setBoardId(board.getId());
                    boardFile.setOriginalFilename(newFiles.get(i).getOriginalFilename());
                    boardFile.setStoredFilename(uploadedFiles.get(i));
                    boardFile.setFileSize(newFiles.get(i).getSize());
                    boardFile.setContentType(newFiles.get(i).getContentType());
                    boardFileMapper.insert(boardFile);
                }
            } catch (Exception e) {
                throw new RuntimeException("파일 업로드 실패", e);
            }
        }

        return getBoardWithFiles(board.getId());
    }

    @Transactional
    public void deleteBoard(Long id) {
        List<ImageBoardFile> files = boardFileMapper.findByBoardId(id);
        for (ImageBoardFile file : files) {
            fileUploadUtil.deleteFile(file.getStoredFilename());
        }
        boardFileMapper.deleteByBoardId(id);
        boardMapper.delete(id);
    }


}
