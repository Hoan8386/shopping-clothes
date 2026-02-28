package com.vn.shopping.service;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class MinioStorageService implements StorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.default}")
    private String defaultBucket;

    @Value("${minio.url}")
    private String minioUrl;

    public MinioStorageService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    /**
     * Đảm bảo bucket tồn tại, nếu chưa thì tạo mới
     */
    private void ensureBucketExists(String bucketName) {
        try {
            boolean found = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi kiểm tra/tạo bucket: " + e.getMessage(), e);
        }
    }

    /**
     * Tạo tên file duy nhất để tránh trùng lặp
     */
    private String generateUniqueFileName(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }

    @Override
    public void upload(List<MultipartFile> files) {
        ensureBucketExists(defaultBucket);
        for (MultipartFile file : files) {
            uploadSingleFile(file);
        }
    }

    @Override
    public String uploadSingleFile(MultipartFile file) {
        ensureBucketExists(defaultBucket);
        try {
            String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(defaultBucket)
                            .object(uniqueFileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            // Trả về URL qua backend proxy endpoint /storage/{fileName}
            // để UI có thể truy cập trực tiếp mà không cần kết nối MinIO
            return "/storage/" + uniqueFileName;
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi upload file lên MinIO: " + e.getMessage(), e);
        }
    }

    /**
     * Upload nhiều file và trả về danh sách URL
     */
    public List<String> uploadMultipleFiles(List<MultipartFile> files) {
        ensureBucketExists(defaultBucket);
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadSingleFile(file));
        }
        return urls;
    }

    @Override
    public InputStream download(String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(defaultBucket)
                            .object(fileName)
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi download file từ MinIO: " + e.getMessage(), e);
        }
    }

    @Override
    public String getURL(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(defaultBucket)
                            .object(fileName)
                            .expiry(60 * 60 * 24) // 24 giờ
                            .build());
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi lấy URL file từ MinIO: " + e.getMessage(), e);
        }
    }
}
