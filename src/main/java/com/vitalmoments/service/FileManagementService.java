package com.vitalmoments.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class FileManagementService {

    public static int deleteSelectedFiles(String mainFolderPath, List<String> filesToDelete) {
        int deletedCount = 0;
        for (String fileName : filesToDelete) {
            File fileToDelete = new File(mainFolderPath, fileName);
            if (fileToDelete.exists() && fileToDelete.delete()) {
                deletedCount++;
            }
        }
        return deletedCount;
    }

    public boolean moveSelectedFiles(String mainFolderPath, List<String> filesToMove,
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
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
