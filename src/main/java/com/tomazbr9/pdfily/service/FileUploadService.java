package com.tomazbr9.pdfily.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(FileUploadService.class);

    @Value("${pdfily.upload.temp-dir}")
    private String uploadDir;

    public void uploadFile(MultipartFile file, UserDetails user){

        if(file == null || file.isEmpty()) {
            logger.warn("O arquivo enviado está vazio ou é nulo");
            throw new RuntimeException("O arquivo enviado está vazio ou é nulo");
        }

        Path dirPath = Paths.get(uploadDir);

        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            // Gera um nome seguro e praticamente único para o arquivo que será salvo.
            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Resolve o nome do arquivo em relação ao diretório, produzindo o caminho completo onde o arquivo será escrito.
            Path filePath = dirPath.resolve(filename);

            // Copia o conteúdo do MultipartFile para o filePath.
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            logger.info("Arquivo '{}' salvo temporariamente em: {}", file.getOriginalFilename(), filePath);

        } catch (IOException error) {
            logger.error("Erro ao salvar o arquivo '{}'", file.getOriginalFilename(), error);
            throw new RuntimeException("Falha ao salvar arquivo temporário", error);
        }

    }

}
