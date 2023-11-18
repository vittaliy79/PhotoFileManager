package com.vitalmoments.service;

import com.vitalmoments.config.PhotoFileManagerConfig;
import com.vitalmoments.model.FileComparisonResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileComparisonService {
    private PhotoFileManagerConfig config;

    public List<FileComparisonResult> compareFiles(String mainFolderPath, String subFolderPath) {
        Map<String, String> mainFolderRawFiles = new HashMap<>();

        for (String extension : config.getRawExtensions()) {
            mainFolderRawFiles.putAll(getFileNamesWithExtensions(mainFolderPath, "." + extension));
        }

        Map<String, String> mainFolderJPGFiles = getFileNamesWithExtensions(mainFolderPath, ".jpg");

        // Merge RAW and JPG files from the main folder
        Map<String, String> mainFolderFiles = new HashMap<>(mainFolderRawFiles);
        mainFolderJPGFiles.forEach(mainFolderFiles::putIfAbsent);

        List<String> subFolderFiles = getFileNames(subFolderPath);

        List<FileComparisonResult> comparisonResults = new ArrayList<>();

        for (String mainFile : mainFolderFiles.keySet()) {
            List<String> matchingSubFiles = subFolderFiles.stream()
                    .filter(subFile -> subFile.contains(mainFile))
                    .collect(Collectors.toList());

            comparisonResults.add(new FileComparisonResult(mainFolderFiles.get(mainFile), matchingSubFiles));
        }

        // Sorting the results by the main folder file name
        comparisonResults.sort(Comparator.comparing(FileComparisonResult::getMainFolderFile));

        return comparisonResults;
    }

    private static Map<String, String> getFileNamesWithExtensions(String folderPath, String extension) {
        Map<String, String> fileNames = new HashMap<>();
        File folder = new File(folderPath);

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(extension));
        if (files != null) {
            for (File file : files) {
                String nameWithoutExtension = file.getName().replaceFirst("[.][^.]+$", "");
                fileNames.put(nameWithoutExtension, file.getName());
            }
        }

        return fileNames;
    }

    private static List<String> getFileNames(String folderPath) {
        if (folderPath == null || folderPath.isEmpty()) {
            return new ArrayList<>(); // Return an empty list if the path is null or empty
        }

        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        List<String> fileNames = new ArrayList<>();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }

        return fileNames;
    }
}
