package com.tomazbr9.pdfily.controller;

import com.tomazbr9.pdfily.dto.authDTO.JwtTokenDTO;
import com.tomazbr9.pdfily.dto.authDTO.LoginDTO;
import com.tomazbr9.pdfily.dto.conversionDTO.ConvertRequestDTO;
import com.tomazbr9.pdfily.dto.conversionDTO.ConvertResponseDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserRequestDTO;
import com.tomazbr9.pdfily.service.AuthService;
import com.tomazbr9.pdfily.service.ConversionService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/convert")
public class ConversionController {

    @Autowired
    private ConversionService service;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ConversionService.class);

    @PostMapping
    public ResponseEntity<ConvertResponseDTO> convert(@RequestBody ConvertRequestDTO request, @AuthenticationPrincipal UserDetails userDetails){

        ConvertResponseDTO response = service.convertToPDF(request, userDetails);

        logger.info("Conversão salva com sucesso.");

        return ResponseEntity.ok(response);
    }

}
