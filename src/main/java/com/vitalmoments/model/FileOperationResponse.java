package com.vitalmoments.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileOperationResponse {
    private boolean success;
    private int deletedCount;

    public FileOperationResponse(boolean success) {
        this.success = success;
    }
}