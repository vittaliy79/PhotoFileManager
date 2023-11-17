package com.vitalmoments.service;

import java.util.List;

public class FileComparisonResult {
    private String mainFolderFile;
    private List<String> subFolderFiles;

    public FileComparisonResult(String mainFolderFile, List<String> subFolderFiles) {
        this.mainFolderFile = mainFolderFile;
        this.subFolderFiles = subFolderFiles;
    }

    // Getters and Setters
    public String getMainFolderFile() {
        return mainFolderFile;
    }

    public void setMainFolderFile(String mainFolderFile) {
        this.mainFolderFile = mainFolderFile;
    }

    public List<String> getSubFolderFiles() {
        return subFolderFiles;
    }

    public void setSubFolderFiles(List<String> subFolderFiles) {
        this.subFolderFiles = subFolderFiles;
    }
}
