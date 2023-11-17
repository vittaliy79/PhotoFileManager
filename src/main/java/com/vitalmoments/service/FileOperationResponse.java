package com.vitalmoments.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileOperationResponse {
    private boolean success;
    private int deletedCount;
}