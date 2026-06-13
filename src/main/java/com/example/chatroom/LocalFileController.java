package com.example.chatroom;

import java.io.IOException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/files")
public class LocalFileController {

    private final LocalFileStorage localFileStorage;

    public LocalFileController(LocalFileStorage localFileStorage) {
        this.localFileStorage = localFileStorage;
    }

    @PostMapping
    public FileUploadResponse upload(@RequestParam("file") MultipartFile file) throws IOException {
        LocalFileStorage.StoredFile storedFile = localFileStorage.save(file);
        return new FileUploadResponse(storedFile.getFileName(), storedFile.getSize(), storedFile.getPath());
    }

    public static class FileUploadResponse {
        private final String fileName;
        private final long size;
        private final String path;

        public FileUploadResponse(String fileName, long size, String path) {
            this.fileName = fileName;
            this.size = size;
            this.path = path;
        }

        public String getFileName() {
            return fileName;
        }

        public long getSize() {
            return size;
        }

        public String getPath() {
            return path;
        }
    }
}
