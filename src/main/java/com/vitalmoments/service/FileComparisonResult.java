package com.vitalmoments.service;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FileComparisonResult {
    private String mainFolderFile;
    private List<String> subFolderFiles;
}
