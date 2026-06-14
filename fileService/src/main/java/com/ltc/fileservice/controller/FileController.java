package com.ltc.fileservice.controller;

import com.ltc.fileservice.fileServiceS.FileServiceImpl;
import com.ltc.fileservice.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.apache.tomcat.util.http.fileupload.FileUploadBase.MULTIPART_FORM_DATA;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileServiceImpl fileServiceImpl;

    @PostMapping(value = "/upload", consumes = MULTIPART_FORM_DATA)
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public ResponseEntity<FileUploadResponse> upload (@RequestParam("file")
            MultipartFile file) {return ResponseEntity.ok(fileServiceImpl.upload(file)
        );
    }
}
