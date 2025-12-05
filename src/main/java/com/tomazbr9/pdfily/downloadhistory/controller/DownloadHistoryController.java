package com.tomazbr9.pdfily.downloadhistory.controller;

import com.tomazbr9.pdfily.dto.downloadDTO.DownloadFileDTO;
import com.tomazbr9.pdfily.dto.downloadDTO.DownloadResponseDTO;
import com.tomazbr9.pdfily.downloadhistory.service.DownloadHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/download")
public class DownloadHistoryController {

    @Autowired
    DownloadHistoryService service;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DownloadHistoryController.class);

    @GetMapping("/{conversionId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable UUID conversionId, @AuthenticationPrincipal UserDetails userDetails){

        DownloadFileDTO downloadFile = service.saveDownloadToHistory(conversionId, userDetails);

        logger.info("Registro de download salvo com sucesso.");

        return ResponseEntity.ok()
                .headers(downloadFile.headers())
                .contentLength(downloadFile.fileSize())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(downloadFile.resource());

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
        logger.info("Registro de Download deletado com sucesso.");
        return ResponseEntity.noContent().build();
    }

}
