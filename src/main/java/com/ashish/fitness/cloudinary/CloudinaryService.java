package com.ashish.fitness.cloudinary;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryService {
    Map upload(MultipartFile file, String folder);
    Map delete(String publicId);
}
