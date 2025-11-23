package com.tomazbr9.pdfily.fileupload.service;

import com.tomazbr9.pdfily.exception.FailedToSaveTemporaryFileException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    // Cria um diretorio temporario caso ele não exista
    public void createDirectoryIfNeeded(Path dirPath){
        try {
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }
        }
        catch(IOException error){
            logger.error("Erro ao criar diretorio: '{}'", dirPath, error);
            throw new FailedToSaveTemporaryFileException("Erro ao criar diretorio temporario.");
        }
    }

    // Copia o conteudo do arquivo enviado para o diretorio
    public void saveFileInDisk(MultipartFile file, Path filePath) {
        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException error){
            logger.error("Erro ao salvar arquivo {} em disco.", filePath, error);
            throw new FailedToSaveTemporaryFileException("Falha ao salvar arquivo temporário.");
        }
    }
}
