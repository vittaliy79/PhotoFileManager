package com.vitalmoments.service;

import org.springframework.stereotype.Service;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FileComparisonService {

    public List<FileComparisonResult> compareFiles(String mainFolderPath, String subFolderPath) {
        Map<String, String> mainFolderCR2Files = getFileNamesWithExtensions(mainFolderPath, ".cr2");
        Map<String, String> mainFolderJPGFiles = getFileNamesWithExtensions(mainFolderPath, ".jpg");
        List<String> subFolderFiles = getFileNames(subFolderPath);

        // Merge CR2 and JPG files from the main folder
        Map<String, String> mainFolderFiles = new HashMap<>(mainFolderCR2Files);
        mainFolderJPGFiles.forEach(mainFolderFiles::putIfAbsent);

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

    private Map<String, String> getFileNamesWithExtensions(String folderPath, String extension) {
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

    private List<String> getFileNames(String folderPath) {
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
