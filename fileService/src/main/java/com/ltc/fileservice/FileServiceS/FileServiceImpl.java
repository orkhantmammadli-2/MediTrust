package com.ltc.fileservice.FileServiceS;

import com.ltc.fileservice.dto.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileServiceMethods {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;


    @Override
    public FileUploadResponse upload(MultipartFile file) {
        List<String> allowed =
                List.of("image/jpeg", "image/png");
        if (!allowed.contains(
                file.getContentType())) {
            throw new RuntimeException(
                    "Only JPEG and PNG allowed"
            );
        }
        try {

            String fileName =
                    UUID.randomUUID() +
                            "-" +
                            file.getOriginalFilename();

            PutObjectRequest request =
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(
                            file.getBytes()
                    )
            );

            String fileUrl =
                    "https://"
                            + bucketName
                            + ".s3."
                            + "eu-north-1"
                            + ".amazonaws.com/"
                            + fileName;

            return new FileUploadResponse(fileUrl);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Upload failed"
            );
        }
    }
}