package com.tomazbr9.pdfily.conversion.controller;

import com.tomazbr9.pdfily.dto.conversionDTO.ConvertRequestDTO;
import com.tomazbr9.pdfily.dto.conversionDTO.ConvertResponseDTO;
import com.tomazbr9.pdfily.conversion.service.ConversionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Conversão de Arquivos",
        description = "Endpoints responsáveis por converter arquivos para PDF"
)
@RestController
@RequestMapping("api/v1/convert")
public class ConversionController {

    @Autowired
    private ConversionService service;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConversionController.class);

    @PostMapping
    @Operation(
            summary = "Converte um arquivo para PDF",
            description = "Recebe um objeto contendo os dados necessários para conversão e retorna o resultado em PDF."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Conversão realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConvertResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos enviados na requisição"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Não autorizado (token inválido ou ausente)"
            ),
    })
    public ResponseEntity<ConvertResponseDTO> convert(

            @Parameter(
                    description = "Objeto contendo os parâmetros para conversão",
                    required = true
            )
            @RequestBody ConvertRequestDTO request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {

        ConvertResponseDTO response = service.convertToPDF(request, userDetails);

        logger.info("Conversão salva com sucesso.");

        return ResponseEntity.ok(response);
    }
}

