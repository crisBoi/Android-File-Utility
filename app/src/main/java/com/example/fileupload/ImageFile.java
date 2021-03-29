package com.example.fileupload;

import java.io.File;

public class ImageFile {
    private java.io.File file;
    private String currentPhotoPath;

    public ImageFile(java.io.File file, String currentPhotoPath) {
        this.file = file;
        this.currentPhotoPath = currentPhotoPath;
    }

    public File getFile() {
        return file;
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }
}