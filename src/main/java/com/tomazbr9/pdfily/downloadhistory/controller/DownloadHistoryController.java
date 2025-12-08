package com.tomazbr9.pdfily.downloadhistory.controller;

import com.tomazbr9.pdfily.dto.downloadDTO.DownloadFileDTO;
import com.tomazbr9.pdfily.dto.downloadDTO.DownloadResponseDTO;
import com.tomazbr9.pdfily.downloadhistory.service.DownloadHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

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


@Tag(
        name = "Histórico de Downloads",
        description = "Endpoints para baixar arquivos convertidos e gerenciar o histórico de downloads"
)
@RestController
@RequestMapping("api/v1/download")
public class DownloadHistoryController {

    @Autowired
    private DownloadHistoryService service;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(DownloadHistoryController.class);

    @GetMapping("/{conversionId}")
    @Operation(
            summary = "Realiza o download de um arquivo",
            description = "Baixa o arquivo convertido usando seu ID e registra o histórico de download."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Download realizado com sucesso",
                    content = @Content(
                            mediaType = "application/octet-stream",
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID informado é inválido"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado (token inválido ou ausente)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Arquivo ou conversão não encontrada"
            )
    })

    public ResponseEntity<Resource> downloadFile(
            @PathVariable
            @Parameter(description = "ID da conversão associada ao arquivo a ser baixado", required = true)
            UUID conversionId,

            @AuthenticationPrincipal UserDetails userDetails
    ) {

        DownloadFileDTO downloadFile = service.saveDownloadToHistory(conversionId, userDetails);

        logger.info("Registro de download salvo com sucesso.");

        return ResponseEntity.ok()
                .headers(downloadFile.headers())
                .contentLength(downloadFile.fileSize())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(downloadFile.resource());
    }

    @GetMapping
    @Operation(
            summary = "Lista todo o histórico de downloads",
            description = "Retorna todos os registros de arquivos baixados pelo usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DownloadResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado"
            )
    })

    public ResponseEntity<List<DownloadResponseDTO>> getAllDownloadHistory(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<DownloadResponseDTO> response = service.getAllDownloadHistory(userDetails);

        logger.info("Listando todos os registros de download...");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{downloadId}")
    @Operation(
            summary = "Deleta um registro de download",
            description = "Remove um item do histórico de downloads com base no ID informado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Registro deletado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID informado é inválido"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Registro não encontrado"
            )
    })

    public ResponseEntity<Void> deleteDownloadLog(
            @PathVariable
            @Parameter(description = "ID do registro de download a ser deletado", required = true)
            UUID downloadId,

            @AuthenticationPrincipal UserDetails userDetails
    ) {

        service.deleteDownloadLog(downloadId, userDetails);

        logger.info("Registro de Download deletado com sucesso.");

        return ResponseEntity.noContent().build();
    }

}

