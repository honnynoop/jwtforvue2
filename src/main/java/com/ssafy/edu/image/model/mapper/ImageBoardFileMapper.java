package com.ssafy.edu.image.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.edu.image.model.ImageBoardFile;

import java.util.List;

@Mapper
public interface ImageBoardFileMapper {
    void insert(ImageBoardFile boardFile);
    void insertBatch(List<ImageBoardFile> files);
    ImageBoardFile findById(Long id);
    List<ImageBoardFile> findByBoardId(Long boardId);
    List<ImageBoardFile> findByIds(List<Long> ids);
    void deleteById(Long id);
    void deleteByIds(List<Long> ids);
    void deleteByBoardId(Long boardId);
    List<String> findStoredFilenamesByBoardId(Long boardId);
}