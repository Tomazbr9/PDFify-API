package com.tomazbr9.pdfily.controller;

import com.tomazbr9.pdfily.dto.authDTO.JwtTokenDTO;
import com.tomazbr9.pdfily.dto.authDTO.LoginDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserRequestDTO;
import com.tomazbr9.pdfily.service.AuthService;
import com.tomazbr9.pdfily.service.ConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/convert")
public class ConversionController {

    @Autowired
    private ConversionService service;

    @PostMapping
    public void convert(){

    }

}
