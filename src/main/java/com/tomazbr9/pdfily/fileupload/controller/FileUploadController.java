package com.tomazbr9.pdfily.fileupload.controller;

import com.tomazbr9.pdfily.dto.fileDTO.FileResponseDTO;
import com.tomazbr9.pdfily.fileupload.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(
        name = "Envio de Arquivos",
        description = "Opperação de envio de arquivos para o sistema."
)

@RestController
@RequestMapping("/api/v1/files")
public class FileUploadController {

    @Autowired
    FileUploadService service;


    @Operation(
            summary = "Faz upload de um arquivo",
            description = "Recebe um arquivo multipart e realiza o upload, retornando os metadados do arquivo salvo."
    )

    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Upload realizado com sucesso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = FileResponseDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Arquivo inválido ou erro de validação"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Não autorizado (token inválido ou ausente)"
                    ),

                    @ApiResponse(
                            responseCode = "500",
                            description = "Erro interno durante upload do arquivo"
                    )

            }
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(
            @RequestParam("file")
            @Parameter(
                    description = "Arquivo a ser enviado (multipart/form-data)",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            MultipartFile file,
            @AuthenticationPrincipal UserDetails userDetails){

        FileResponseDTO response = service.uploadFile(file, userDetails);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
