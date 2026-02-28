package com.vn.shopping.controller;

import java.io.InputStream;
import java.net.URLConnection;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vn.shopping.service.MinioStorageService;

@RestController
@RequestMapping("/storage")
public class StorageController {

    private final MinioStorageService minioStorageService;

    public StorageController(MinioStorageService minioStorageService) {
        this.minioStorageService = minioStorageService;
    }

    /**
     * Endpoint phục vụ hình ảnh/file từ MinIO cho UI.
     * URL: /storage/{fileName}
     * Ví dụ: /storage/abc-123.jpg
     * 
     * Endpoint này đã được whitelist trong SecurityConfiguration
     * nên không cần xác thực.
     */
    @GetMapping("/{fileName:.+}")
    public ResponseEntity<InputStreamResource> getFile(@PathVariable("fileName") String fileName) {
        try {
            InputStream inputStream = minioStorageService.download(fileName);

            // Xác định content type từ tên file
            String contentType = URLConnection.guessContentTypeFromName(fileName);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .header(HttpHeaders.CACHE_CONTROL, "max-age=86400") // Cache 1 ngày
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
