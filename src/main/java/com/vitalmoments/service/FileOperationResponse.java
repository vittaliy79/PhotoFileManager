package com.vitalmoments.service;

public class FileOperationResponse {
    private boolean success;
    private int deletedCount;

    // Constructor, getters, and setters
    public FileOperationResponse(boolean success, int deletedCount) {
        this.success = success;
        this.deletedCount = deletedCount;
    }

    public FileOperationResponse(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(int deletedCount) {
        this.deletedCount = deletedCount;
    }
}