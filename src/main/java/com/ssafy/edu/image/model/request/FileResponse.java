package com.ssafy.edu.image.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponse {
    private String fileName;
    private String fileDownloadUri;
    private long size;
}