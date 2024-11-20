package com.ssafy.edu.image.model.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.edu.image.model.ImageBoard;

import java.util.List;

@Mapper
public interface ImageBoardMapper {
    void insert(ImageBoard board);
    ImageBoard findById(Long id);
    List<ImageBoard> findAll(@Param("searchType") String searchType, @Param("keyword") String keyword);
    void update(ImageBoard board);
    void delete(Long id);
    void incrementViewCount(Long id);
}