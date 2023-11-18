package com.vitalmoments.service;

import com.vitalmoments.config.Constants;
import com.vitalmoments.config.PhotoFileManagerConfig;
import com.vitalmoments.model.ImageRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class ImageService {
    private PhotoFileManagerConfig config;

    public ResponseEntity<Resource> getImage(ImageRequest request) {
        Path imagePath = Paths.get(request.getMainFolderPath()).resolve(request.getFilename()).normalize();

        if (!imagePath.toFile().exists() || !isPathSecure(imagePath, request.getMainFolderPath())) {
            log.warn("Image not found or path not secure: {}", imagePath);
            return ResponseEntity.notFound().build();
        }

        try {
            if (request.isCallWithConversion() && isRawExtension(request.getFilename())) {
                Process process = getImageMagickProcess(imagePath);

                try (InputStream is = process.getInputStream();
                     ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                    long startTime = System.currentTimeMillis();

                    int nRead;
                    byte[] data = new byte[1024];
                    while ((nRead = is.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }
                    buffer.flush();
                    ByteArrayResource imageResource = new ByteArrayResource(buffer.toByteArray());

                    long elapsedTime = System.currentTimeMillis() - startTime;
                    log.info("ImageMagick processed the file: {} in {} ms", request.getFilename(), elapsedTime);
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
                }
            } else {
                Resource image = new FileSystemResource(imagePath);
                return ResponseEntity.ok().body(image);
            }
        } catch (Exception exception) {
            log.error("Error processing image request", exception);
            return ResponseEntity.internalServerError().build();
        }
    }

    private Process getImageMagickProcess(Path imagePath) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        // Get the process's environment variables as a mutable map
        Map<String, String> environment = processBuilder.environment();

        // Set or modify environment variables
        String magickHome = config.getMagickHome();
        environment.put("MAGICK_HOME", magickHome);
        environment.put("DYLD_LIBRARY_PATH", magickHome + Constants.IMAGE_MAGICK_LIB_PATH);
        processBuilder.command(magickHome + Constants.IMAGE_MAGICK_CONVERT_PATH, imagePath.toString(), Constants.IMAGE_MAGICK_JPG_OUTPUT_STREAM);

        return processBuilder.start();
    }

    private static boolean isPathSecure(Path imagePath, String mainFolderPath) {
        Path mainFolder = Paths.get(mainFolderPath).normalize();
        return imagePath.startsWith(mainFolder);
    }

    private boolean isRawExtension(String filename) {
        String fileExtension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        return config.getRawExtensions().contains(fileExtension);
    }
}
