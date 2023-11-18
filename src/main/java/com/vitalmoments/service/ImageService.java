package com.vitalmoments.service;

import com.vitalmoments.model.ImageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
public class ImageService {
    public static final String IMAGE_MAGICK_JPG_OUTPUT_STREAM = "jpg:-";
    public static final String IMAGE_MAGICK_CONVERT_PATH = "/bin/convert";
    public static final String IMAGE_MAGICK_LIB_PATH = "/lib";
    public static final String CR2_RAW_EXTENSION = ".cr2";

    @Value("${image.magick.home}")
    private String magickHome;

    public ResponseEntity<Resource> getImage(ImageRequest request) {
        Path imagePath = Paths.get(request.getMainFolderPath()).resolve(request.getFilename()).normalize();

        if (!imagePath.toFile().exists() || !isPathSecure(imagePath, request.getMainFolderPath())) {
            log.warn("Image not found or path not secure: {}", imagePath);
            return ResponseEntity.notFound().build();
        }

        try {
            if (request.isCallWithConversion() && request.getFilename().toLowerCase().endsWith(CR2_RAW_EXTENSION)) {
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
        environment.put("MAGICK_HOME", magickHome);
        environment.put("DYLD_LIBRARY_PATH", magickHome + IMAGE_MAGICK_LIB_PATH);
        processBuilder.command(magickHome + IMAGE_MAGICK_CONVERT_PATH, imagePath.toString(), IMAGE_MAGICK_JPG_OUTPUT_STREAM);

        return processBuilder.start();
    }

    private static boolean isPathSecure(Path imagePath, String mainFolderPath) {
        Path mainFolder = Paths.get(mainFolderPath).normalize();
        return imagePath.startsWith(mainFolder);
    }
}
