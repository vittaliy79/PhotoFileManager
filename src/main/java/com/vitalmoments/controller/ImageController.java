package com.vitalmoments.controller;

import org.apache.commons.imaging.Imaging;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

@RestController
public class ImageController {

    @PostMapping(value = "/images", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> getImage(@RequestBody ImageRequest request) {
        Path imagePath = Paths.get(request.getMainFolderPath()).resolve(request.getFilename()).normalize();

        if (!imagePath.toFile().exists() || !isPathSecure(imagePath, request.getMainFolderPath())) {
            return ResponseEntity.notFound().build();
        }

        try {
            if (request.getFilename().toLowerCase().endsWith(".cr2")) {
                File cr2File = imagePath.toFile();
                BufferedImage bufferedImage = Imaging.getBufferedImage(cr2File);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", baos);
                byte[] jpgData = baos.toByteArray();
                Resource imageResource = new ByteArrayResource(jpgData);
                return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
            } else {
                Resource image = new FileSystemResource(imagePath);
                return ResponseEntity.ok().body(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


    private boolean isPathSecure(Path imagePath, String mainFolderPath) {
        Path mainFolder = Paths.get(mainFolderPath).normalize();
        return imagePath.startsWith(mainFolder);
    }
}


class ImageRequest {
    private String mainFolderPath;
    private String filename;

    // Getters and Setters

    public String getMainFolderPath() {
        return mainFolderPath;
    }

    public void setMainFolderPath(String mainFolderPath) {
        this.mainFolderPath = mainFolderPath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}