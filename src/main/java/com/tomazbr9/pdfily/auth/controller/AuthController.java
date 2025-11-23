package com.tomazbr9.pdfily.auth.controller;

import com.tomazbr9.pdfily.dto.authDTO.JwtTokenDTO;
import com.tomazbr9.pdfily.dto.authDTO.LoginDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserRequestDTO;
import com.tomazbr9.pdfily.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDTO request){
        service.registerUser(request);
        return new ResponseEntity<>("Usuário Criado com sucesso!",HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> authenticateUser(@RequestBody LoginDTO request){
        JwtTokenDTO token = service.authenticateUser(request);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
