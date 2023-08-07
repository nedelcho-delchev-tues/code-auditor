package com.code.auditor.dtos;

import java.util.List;

public class AssignmentRequestDTO {
    private String title;
    private String description;
    private List<String> specialFiles;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getSpecialFiles() {
        return specialFiles;
    }

    public void setSpecialFiles(List<String> specialFiles) {
        this.specialFiles = specialFiles;
    }
}
