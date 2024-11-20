package com.ssafy.edu.image.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageBoard {
    private Long id;        //auto
    //----------------------------필수 입력
    private String title;
    private String content;
    private Long userId;           //FK 
    //----------------------------
    private Integer viewCount;
    private String createdAt;
    private String updatedAt;
    
    // 추가 필드
    private String authorName;
    private List<ImageBoardFile> files;
	
}