package com.vitalmoments.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FileComparisonResult {
    private String mainFolderFile;
    private List<String> subFolderFiles;
}
