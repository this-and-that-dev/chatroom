package com.example.chatroom;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class LocalFileStorage {

    private final Path uploadDir;

    public LocalFileStorage(@Value("${chatroom.upload-dir:uploads}") Path uploadDir) {
        this.uploadDir = uploadDir;
    }

    public StoredFile save(MultipartFile file) throws IOException {
        String fileName = cleanFileName(file.getOriginalFilename());
        Files.createDirectories(uploadDir);

        Path destination = uploadDir.resolve(fileName).normalize();
        if (!destination.startsWith(uploadDir.normalize())) {
            throw new IOException("Invalid file path");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        return new StoredFile(fileName, file.getSize(), destination.toAbsolutePath().toString());
    }

    private String cleanFileName(String originalFileName) throws IOException {
        String fileName = StringUtils.cleanPath(originalFileName == null ? "" : originalFileName);
        if (fileName.isBlank() || fileName.contains("..")) {
            throw new IOException("Invalid file name");
        }
        return Path.of(fileName).getFileName().toString();
    }

    public static class StoredFile {
        private final String fileName;
        private final long size;
        private final String path;

        public StoredFile(String fileName, long size, String path) {
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
