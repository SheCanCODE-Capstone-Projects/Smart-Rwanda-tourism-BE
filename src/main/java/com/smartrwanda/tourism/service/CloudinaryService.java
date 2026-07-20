package com.smartrwanda.tourism.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    public String uploadImage(MultipartFile file, String folder) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "smart-rwanda/" + folder,
                            "resource_type", "image"
                    )
            );
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }

    public List<String> uploadMultipleImages(List<MultipartFile> files, String folder) {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                imageUrls.add(uploadImage(file, folder));
            }
        }
        return imageUrls;
    }

    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image: " + e.getMessage());
        }
    }

    public String uploadFromUrl(String imageUrl, String folder) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    imageUrl,
                    ObjectUtils.asMap(
                            "folder", "smart-rwanda/" + folder,
                            "resource_type", "image"
                    )
            );
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image from URL: " + e.getMessage());
        }
    }
}