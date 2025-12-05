package com.tomazbr9.pdfily.util;

import com.tomazbr9.pdfily.enums.TargetFormat;
import com.tomazbr9.pdfily.exception.FileSizeException;
import com.tomazbr9.pdfily.exception.UnsupportedFileFormatException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    // Obtêm a extensão do nome do arquivo
    public static String getSafeExtension(String originalName){

        String extension = FilenameUtils.getExtension(originalName);

        if(extension == null || extension.isBlank()){
            logger.warn("O arquivo {} não possui extensão.", originalName);
            throw new UnsupportedFileFormatException("O arquivo não possui extensão.");
        }

        if(!TargetFormat.isSupported(extension)){
            logger.warn("Formato do arquivo enviado não é suportado: " + extension);
            throw new UnsupportedFileFormatException("Formato não suportado: " + extension);
        }

        return extension.toLowerCase();
    }

    // Gera um novo nome para o arquivo enviado
    public static String generateSafeFilename(String extension){
        return UUID.randomUUID().toString() + "." + extension;
    }

    // Obtem o tamaho do arquivo em byts
    public static long getSizeInBytes(Path filePath){

        try {
            return Files.size(filePath);
        } catch (IOException error){
            logger.error("Falha ao obter tamanho do arquivo.", error);
            throw new FileSizeException("Erro oa obter tamamho do arquivo.");
        }
    }

    // Calcula e retorna o tamanho do documento em mega byts
    public static Double calculateFileSizeInMB(long fileSizeInByts){
        return  (double) fileSizeInByts / (1024 * 1024);
    }
}
