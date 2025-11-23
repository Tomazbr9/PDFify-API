package com.tomazbr9.pdfily.downloadhistory.controller;

import com.tomazbr9.pdfily.dto.downloadDTO.DownloadResponseDTO;
import com.tomazbr9.pdfily.exception.ConversionNotFoundException;
import com.tomazbr9.pdfily.exception.FileSizeException;
import com.tomazbr9.pdfily.exception.ReadsFileInBytsException;
import com.tomazbr9.pdfily.conversion.model.ConversionModel;
import com.tomazbr9.pdfily.conversion.repository.ConversionRepository;
import com.tomazbr9.pdfily.downloadhistory.service.DownloadHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/download")
public class DownloadHistoryController {

    @Autowired
    ConversionRepository conversionRepository;

    @Autowired
    DownloadHistoryService service;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DownloadHistoryController.class);

    @GetMapping("/start/{conversionId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID conversionId, @AuthenticationPrincipal UserDetails userDetails){

        service.saveDownloadToHistory(conversionId, userDetails);

        logger.info("Registro de download salvo com sucesso.");

        Path filePath = getFilePath(conversionId);

        InputStreamResource resource = convertFileToBytes(filePath);

        HttpHeaders headers = configureHeadersHttp(filePath.getFileName().toString());

        long fileSize = getFileSize(filePath);


        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileSize)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

    }

    @GetMapping
    public ResponseEntity<List<DownloadResponseDTO>> getAllDownloadHistory(@AuthenticationPrincipal UserDetails userDetails){
        List<DownloadResponseDTO> response = service.getAllDownloadHistory(userDetails);

        logger.info("Listando todos os registros de download...");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{downloadId}")
    public ResponseEntity<Void> deleteDownloadLog(@PathVariable UUID downloadId, @AuthenticationPrincipal UserDetails userDetails){
        service.deleteDownloadLog(downloadId, userDetails);
        return ResponseEntity.noContent().build();
    }

    private Path getFilePath(UUID uuid){
        ConversionModel conversionModel = conversionRepository.findById(uuid).orElseThrow(() -> new ConversionNotFoundException("Conversão não encontrada."));

        logger.info("Registro de Download deletado com sucesso.");

        return Path.of(conversionModel.getOutputPath());
    }

    private InputStreamResource convertFileToBytes(Path filePath){
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

    private HttpHeaders configureHeadersHttp(String filename){
        // Configura cabeçalho para download do arquivo

        HttpHeaders headers = new HttpHeaders();

        // Define a disposição como "attachment" (anexo) para forçar o download
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        // Define o tipo de conteúdo (pode ser dinâmico com base no tipo do arquivo)
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return headers;
    }

    private long getFileSize(Path filePath){
        try {
            return Files.size(filePath);
        }
        catch (IOException error) {
            logger.error("Erro ao obter tamanho do arquivo: {}", filePath.getFileName().toString(), error);
            throw new FileSizeException("Erro ao obter tamanho do arquivo");
        }
    }


}
