package com.tomazbr9.pdfily.auth.controller;

import com.tomazbr9.pdfily.dto.authDTO.JwtTokenDTO;
import com.tomazbr9.pdfily.dto.authDTO.LoginDTO;
import com.tomazbr9.pdfily.dto.userDTO.UserRequestDTO;
import com.tomazbr9.pdfily.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;


@Tag(
        name = "Autenticação",
        description = "Operações para registro e login de usuários"
)
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um novo usuário no sistema com dados fornecidos"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso."),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos.",
                            content = @Content(schema = @Schema(hidden = true)))
            }
    )
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados necessários para registrar o usuário",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRequestDTO.class))
            )
            @RequestBody UserRequestDTO request){
        service.registerUser(request);
        return new ResponseEntity<>("Usuário Criado com sucesso!",HttpStatus.CREATED);
    }

    @Operation(
            summary = "Autenticar usuário",
            description = "Realiza login com e-mail e senha, retornando um token JWT válido."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(schema = @Schema(implementation = JwtTokenDTO.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/login")
    public ResponseEntity<JwtTokenDTO> authenticateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de acesso (nome de usuário e senha)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginDTO.class))
            )
            @RequestBody LoginDTO request){
        JwtTokenDTO token = service.authenticateUser(request);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
