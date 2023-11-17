package com.vitalmoments.controller;

import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
public class ImageController {

    public static final String IMAGE_MAGICK_CONVERT_PATH = "/Users/vown/Documents/Projects/devtools/ImageMagick-7.0.10/bin/convert";

    @PostMapping(value = "/images", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> getImage(@RequestBody ImageRequest request) {
        Path imagePath = Paths.get(request.getMainFolderPath()).resolve(request.getFilename()).normalize();

        if (!imagePath.toFile().exists() || !isPathSecure(imagePath, request.getMainFolderPath())) {
            return ResponseEntity.notFound().build();
        }

        try {
            if (request.isCallWithConversion() && request.getFilename().toLowerCase().endsWith(".cr2")) {
                ProcessBuilder processBuilder = new ProcessBuilder();
                // Get the process's environment variables as a mutable map
                Map<String, String> environment = processBuilder.environment();

                // Set or modify environment variables
                environment.put("MAGICK_HOME", "/Users/vown/Documents/Projects/devtools/ImageMagick-7.0.10");
                environment.put("PATH", "$MAGICK_HOME/bin:$PATH");
                environment.put("DYLD_LIBRARY_PATH", "/Users/vown/Documents/Projects/devtools/ImageMagick-7.0.10/lib");
                processBuilder.command(IMAGE_MAGICK_CONVERT_PATH, imagePath.toString(), "jpg:-");

                Process process = processBuilder.start();
              /*  int exitCode = process.waitFor();  // Wait for the process to complete

                if (exitCode != 0) {
                    String errorOutput = new String(process.getErrorStream().readAllBytes());
                    if (!errorOutput.isEmpty()) {
                        System.err.println("ImageMagick error: " + errorOutput);
                    }
                    return ResponseEntity.internalServerError().build();
                }*/

                try (InputStream is = process.getInputStream();
                     ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

                    int nRead;
                    byte[] data = new byte[1024];
                    while ((nRead = is.read(data, 0, data.length)) != -1) {
                        buffer.write(data, 0, nRead);
                    }

                    buffer.flush();
                    byte[] jpegData = buffer.toByteArray();
                    ByteArrayResource imageResource = new ByteArrayResource(jpegData);

                   /* try {
                        String filePath = "/Users/vown/Documents/Projects/PhotoFileManagerGit/" +
                                FilenameUtils.removeExtension(request.getFilename()) + ".jpg";
                        saveToFile(imageResource, filePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Handle the exception
                    }*/
                    System.out.println("ImageMagick processed the file: " + request.getFilename());
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageResource);
                }
            } else {
                Resource image = new FileSystemResource(imagePath);
                return ResponseEntity.ok().body(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    public static void saveToFile(ByteArrayResource byteArrayResource, String filePath) throws IOException {
        byte[] data = byteArrayResource.getByteArray();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(data);
        }
    }

    private boolean isPathSecure(Path imagePath, String mainFolderPath) {
        Path mainFolder = Paths.get(mainFolderPath).normalize();
        return imagePath.startsWith(mainFolder);
    }
}


@Data
class ImageRequest {
    private String mainFolderPath;
    private String filename;
    private boolean callWithConversion;
}