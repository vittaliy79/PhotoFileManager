package com.vitalmoments.model;

import lombok.Data;

@Data
public class ImageRequest {
    private String mainFolderPath;
    private String filename;
    private boolean callWithConversion;
}
