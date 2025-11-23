package com.gtech.Ecommerce.dto.upload;

import org.springframework.web.multipart.MultipartFile;

public class FileDTO {

    private MultipartFile file;

    public FileDTO() {
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
