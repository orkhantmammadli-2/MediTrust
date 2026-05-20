package com.ltc.fileservice.FileServiceS;

import com.ltc.fileservice.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileServiceMethods {
    FileUploadResponse upload(MultipartFile file);
}
