package com.ashish.fitness.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryServiceImp implements CloudinaryService{
@Autowired
    private Cloudinary cloudinary;
    @Override
    public Map upload(MultipartFile file, String folder) {
        try {

            // Upload the image to Cloudinary
            Map<String, Object> options = ObjectUtils.asMap("folder", folder);
            Map<String, Object> data = cloudinary.uploader().upload(file.getBytes(), options);

            // Save the URL to the database
            String imageUrl = (String) data.get("secure_url");
            System.out.println("Image Url"+imageUrl);
            return data;
        } catch (IOException e) {
            throw new RuntimeException("Image uploading failed !");
        }
    }


    @Override
    public Map delete(String publicId) {
        try {
            // Delete the image from Cloudinary
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            System.out.println("Delete result: " + result);
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Image deletion failed!", e);
        }
    }
}
