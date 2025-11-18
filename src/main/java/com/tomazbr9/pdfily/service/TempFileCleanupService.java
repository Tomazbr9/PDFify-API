package com.tomazbr9.pdfily.service;

import com.tomazbr9.pdfily.exception.ClearTemporaryFilesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

@Service
public class TempFileCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(TempFileCleanupService.class);

    @Value("${pdfily.upload.temp-dir}")
    private String uploadDir;

    @Scheduled(fixedRate = 300000)
    public void cleanTempFiles(){
        try {

            Path dirPath = Path.of(uploadDir);

            if(!Files.exists(dirPath)) return;

            // lista todos os arquivos dentro do diretorio temporario e filtra cada um.
            Files.list(dirPath)
                    .filter(Files::isRegularFile)
                    .filter(this::isExpired)
                    .forEach(this::deleteFileSafely);
        }
        catch(IOException error){
            logger.error("Erro ao limpar arquivos temporários", error);
            throw new ClearTemporaryFilesException("Erro ao limpar arquivos temporários.");
        }
    }

    private boolean isExpired(Path filePath){
        // Obtem o time da ultima modificação do arquivo, subtrai, divide e compara para identificar se pode limpar.
        try {
            FileTime lastModifiedTime = Files.getLastModifiedTime(filePath);
            long ageInMinutes = (System.currentTimeMillis() - lastModifiedTime.toMillis() / (1000 * 60));
            return ageInMinutes > 5;
        }
        catch (IOException error){
            logger.warn("Não foi possível verificar o tempo do arquivo: {}", filePath);
            return false;
        }
    }

    private void deleteFileSafely(Path filePath){
        // Limpa os arquivos ja expirados
        try {
            Files.deleteIfExists(filePath);
            logger.info("Arquivo temporário removido: {}", filePath.getFileName());
        } catch (IOException e) {
            logger.warn("Falha ao deletar arquivo temporário: {}", filePath, e);
        }
    }
}
