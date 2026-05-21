package com.ltc.fileservice.fileServiceS;

import com.ltc.fileservice.dto.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileServiceMethods {
    FileUploadResponse upload(MultipartFile file);
}
