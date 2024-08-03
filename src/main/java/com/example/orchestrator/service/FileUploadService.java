package com.example.orchestrator.service;

import com.example.orchestrator.dto.ImageDto;
import com.example.orchestrator.property.FileUploadProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    private final Path fileStorageLocation;

    @Autowired
    public FileUploadService(FileUploadProperties fileUploadProperties) {
        this.fileStorageLocation = Paths.get(fileUploadProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public ImageDto storeFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String newFilename = System.currentTimeMillis() + "_" + originalFilename;

        try {
            if (newFilename.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + newFilename);
            }

            Path targetLocation = this.fileStorageLocation.resolve(newFilename);
            Files.copy(file.getInputStream(), targetLocation);

            String uploadedPath = targetLocation.toString();
            long fileSize = Files.size(targetLocation);

            return new ImageDto(uploadedPath, originalFilename, fileExtension, fileSize);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + newFilename + ". Please try again!", ex);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        }
        int lastIndexOfDot = filename.lastIndexOf('.');
        return (lastIndexOfDot == -1) ? "" : filename.substring(lastIndexOfDot + 1);
    }
}
