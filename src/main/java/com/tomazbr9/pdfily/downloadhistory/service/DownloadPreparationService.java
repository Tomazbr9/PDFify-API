package com.tomazbr9.pdfily.downloadhistory.service;

import com.tomazbr9.pdfily.exception.ReadsFileInBytsException;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class DownloadPreparationService {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DownloadPreparationService.class);


    public InputStreamResource convertFileToBytes(Path filePath){
        // Lê e converte o arquivo para download em byts
        try {
            logger.info("Lendo documento e convertendo em byts...");
            return new InputStreamResource(Files.newInputStream(filePath));

        }
        catch (IOException error){
            logger.error("Erro ao lê arquivo {} em byts", filePath.getFileName(), error);
            throw new ReadsFileInBytsException("Erro ao lê arquivo em byts");
        }
    }

    public HttpHeaders configureHeadersHttp(String filename){
        // Configura cabeçalho para download do arquivo

        HttpHeaders headers = new HttpHeaders();

        // Define a disposição como "attachment" (anexo) para forçar o download
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        // Define o tipo de conteúdo (pode ser dinâmico com base no tipo do arquivo)
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return headers;
    }


}
