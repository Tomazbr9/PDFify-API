package com.tomazbr9.pdfily.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${pdfily.upload.temp-dir}")
    private String uploadDir;

    public void uploadFile(MultipartFile file, UserDetails user){

        if(file == null || file.isEmpty()) {
            throw new RuntimeException("O arquivo está vazio ou é nulo");
        }

        Path dirPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path filePath = dirPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException("Falha ao salvar arquivo temporário", e);
        }

    }

}
