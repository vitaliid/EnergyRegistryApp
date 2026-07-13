package org.example.service;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.config.MinioProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObjectStorageService {

    private final MinioClient minioClient;
    private final MinioProperties properties;

    @PostConstruct
    void ensureBucketExists() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(properties.bucket())
                            .build()
            );

            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(properties.bucket())
                                .build()
                );
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Could not initialize MinIO bucket", ex);
        }
    }

    public StoredObject upload(MultipartFile file) {
        String originalName = Optional.ofNullable(file.getOriginalFilename())
                .orElse("upload.bin");

        String safeName = originalName.replaceAll("[^a-zA-Z0-9._-]", "_");

        String objectKey = "uploads/%s/%s-%s".formatted(
                LocalDate.now(),
                UUID.randomUUID(),
                safeName
        );

        String contentType = Optional.ofNullable(file.getContentType())
                .orElse("application/octet-stream");

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(properties.bucket())
                            .object(objectKey)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(contentType)
                            .build()
            );

            return new StoredObject(
                    properties.bucket(),
                    objectKey,
                    file.getSize(),
                    contentType
            );

        } catch (Exception ex) {
            throw new IllegalStateException(
                    "Could not upload file to MinIO: " + originalName,
                    ex
            );
        }
    }

    public record StoredObject(
            String bucket,
            String objectKey,
            long size,
            String contentType
    ) {
    }
}