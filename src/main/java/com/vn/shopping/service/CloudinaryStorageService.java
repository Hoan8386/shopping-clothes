package com.vn.shopping.service;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

@Service
public class CloudinaryStorageService implements StorageService {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.folder:products}")
    private String uploadFolder;

    public CloudinaryStorageService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public void upload(List<MultipartFile> files) {
        uploadMultipleFiles(files);
    }

    @Override
    public String uploadSingleFile(MultipartFile file) {
        try {
            String publicId = UUID.randomUUID().toString();
            Map<String, Object> options = new HashMap<>();
            options.put("folder", uploadFolder);
            options.put("public_id", publicId);
            options.put("resource_type", "auto");
            options.put("overwrite", false);

            @SuppressWarnings("rawtypes")
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
            Object secureUrl = uploadResult.get("secure_url");
            return secureUrl != null ? secureUrl.toString() : null;
        } catch (Exception e) {
            throw new RuntimeException("Loi khi upload file len Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> uploadMultipleFiles(List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            urls.add(uploadSingleFile(file));
        }
        return urls;
    }

    @Override
    public InputStream download(String fileName) {
        try {
            String url = getURL(fileName);
            return new URL(url).openStream();
        } catch (Exception e) {
            throw new RuntimeException("Loi khi download file tu Cloudinary: " + e.getMessage(), e);
        }
    }

    @Override
    public String getURL(String fileName) {
        return cloudinary.url().secure(true).generate(fileName);
    }
}