package com.vitalmoments.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FileManagementService {

    public static FileOperationResponse deleteSelectedFiles(String mainFolderPath, List<String> filesToDelete) {
        int deletedCount = 0;
        boolean success = true;

        for (String fileName : filesToDelete) {
            File fileToDelete = new File(mainFolderPath, fileName);
            if (fileToDelete.exists() && !fileToDelete.delete()) {
                success = false;
                // Optionally, log the failure
            } else {
                deletedCount++;
            }
        }

        return new FileOperationResponse(success, deletedCount);
    }

    public static FileOperationResponse moveSelectedFiles(String mainFolderPath, List<String> filesToMove,
                                                          String destinationPath) {
        try {
            for (String fileName : filesToMove) {
                File file = new File(mainFolderPath, fileName);
                if (file.exists()) {
                    Files.move(Paths.get(file.getAbsolutePath()),
                            Paths.get(destinationPath, file.getName()),
                            StandardCopyOption.REPLACE_EXISTING);
                }
            }
            return new FileOperationResponse(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new FileOperationResponse(false);
        }
    }
}
